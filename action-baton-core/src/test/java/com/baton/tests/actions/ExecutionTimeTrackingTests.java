/*
 * Copyright 2026 ActionBaton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baton.tests.actions;

import com.baton.entities.ActionExecutionResult;
import com.baton.entities.ActionStatus;
import com.baton.entities.IAction;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.actions.FailingAction;
import com.baton.examples.actions.PrintAction;
import com.baton.exception.ActionBatonException;
import com.baton.executor.impl.ActionBatonBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExecutionTimeTrackingTests {

    private ActionBatonBuilderFactory factory;

    @BeforeEach
    public void init() {
        factory = ActionBatonBuilderFactory.getInstance();
    }

    @Test
    public void testExecutionTimeTracking_SequentialActions() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_1");
        
        factory.getSequenceBuilder()
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .build()
                .execute(context);

        List<ActionExecutionResult> results = context.getResults();
        assertEquals(3, results.size());

        for (Object obj : results) {
            ActionExecutionResult result = (ActionExecutionResult) obj;
            assertNotNull(result.getStartTime(), "Start time should not be null");
            assertNotNull(result.getEndTime(), "End time should not be null");
            assertNotNull(result.getExecutionTimeMs(), "Execution time should not be null");
            assertTrue(result.getExecutionTimeMs() >= 0, "Execution time should be non-negative");
            assertTrue(result.getEndTime().isAfter(result.getStartTime()) || 
                      result.getEndTime().equals(result.getStartTime()),
                      "End time should be after or equal to start time");
        }
    }

    @Test
    public void testExecutionTimeTracking_ParallelActions() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_2");
        
        factory.getParallelBuilder()
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .build()
                .execute(context);

        List<ActionExecutionResult> results = context.getResults();
        assertEquals(3, results.size());

        for (Object obj : results) {
            ActionExecutionResult result = (ActionExecutionResult) obj;
            assertNotNull(result.getStartTime());
            assertNotNull(result.getEndTime());
            assertNotNull(result.getExecutionTimeMs());
            assertTrue(result.getExecutionTimeMs() >= 0);
        }
    }

    @Test
    public void testExecutionTimeTracking_FailedAction() {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_3");
        FailingAction failingAction = new FailingAction("FAILING_ACTION", 2);
        
        assertThrows(Exception.class, () -> {
            factory.getSequenceBuilder()
                    .add(failingAction)
                    .build()
                    .execute(context);
        });

        List<ActionExecutionResult> results = context.getResults();
        assertEquals(1, results.size());
        
        ActionExecutionResult result = (ActionExecutionResult) results.get(0);
        assertEquals(ActionStatus.FAILED, result.getStatus());
        assertNotNull(result.getStartTime());
        assertNotNull(result.getEndTime());
        assertNotNull(result.getExecutionTimeMs());
        assertTrue(result.getExecutionTimeMs() >= 0);
    }

    @Test
    public void testExecutionTimeTracking_TimeOrdering() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_5");
        
        factory.getSequenceBuilder()
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .build()
                .execute(context);

        List<ActionExecutionResult> results = context.getResults();
        assertEquals(3, results.size());

        // Verify that actions execute in order (end time of previous <= start time of next)
        for (int i = 0; i < results.size() - 1; i++) {
            ActionExecutionResult current = (ActionExecutionResult) results.get(i);
            ActionExecutionResult next = (ActionExecutionResult) results.get(i + 1);
            
            assertTrue(current.getEndTime().isBefore(next.getStartTime()) || 
                      current.getEndTime().equals(next.getStartTime()),
                      "Previous action should end before or at the same time as next action starts");
        }
    }

    @Test
    public void testExecutionTimeTracking_InstantValues() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_6");
        
        factory.getSequenceBuilder()
                .add(new PrintAction())
                .build()
                .execute(context);

        ActionExecutionResult result = (ActionExecutionResult) context.getResults().get(0);
        
        // Verify Instant values are valid
        assertNotNull(result.getStartTime());
        assertNotNull(result.getEndTime());
        assertTrue(result.getStartTime().isBefore(Instant.now()) || 
                  result.getStartTime().equals(Instant.now()));
        assertTrue(result.getEndTime().isBefore(Instant.now()) || 
                  result.getEndTime().equals(Instant.now()));
    }

    @Test
    public void testExecutionTimeTracking_CalculationAccuracy() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_7");
        
        // Add a small delay to action execution
        IAction<ActionExecutionContextImpl, Exception> delayedAction = new IAction<ActionExecutionContextImpl, Exception>() {
            @Override
            public String getActionName() {
                return "DELAYED_ACTION";
            }

            @Override
            public void execute(ActionExecutionContextImpl context) throws Exception {
                Thread.sleep(100);
            }
        };
        
        factory.getSequenceBuilder()
                .add(delayedAction)
                .build()
                .execute(context);

        ActionExecutionResult result = (ActionExecutionResult) context.getResults().get(0);
        
        // Verify execution time is approximately 100ms (with tolerance)
        assertTrue(result.getExecutionTimeMs() >= 90, "Execution time should be at least 90ms");
        assertTrue(result.getExecutionTimeMs() <= 200, "Execution time should be at most 200ms");
        
        // Verify calculated duration matches difference between start and end
        long calculatedDuration = java.time.Duration.between(result.getStartTime(), result.getEndTime()).toMillis();
        assertEquals(result.getExecutionTimeMs(), calculatedDuration, 5, // 5ms tolerance
                "Stored execution time should match calculated duration");
    }

    @Test
    public void testExecutionTimeTracking_MultipleResults() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_8");
        
        factory.getSequenceBuilder()
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .add(new PrintAction())
                .build()
                .execute(context);

        List<ActionExecutionResult> results = context.getResults();
        assertEquals(5, results.size());

        // Verify all results have timing information
        for (Object obj : results) {
            ActionExecutionResult result = (ActionExecutionResult) obj;
            assertNotNull(result.getStartTime(), "All results should have start time");
            assertNotNull(result.getEndTime(), "All results should have end time");
            assertNotNull(result.getExecutionTimeMs(), "All results should have execution time");
        }
    }

    @Test
    public void testExecutionTimeTracking_ToStringIncludesTiming() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("TIMING_TEST_9");
        
        factory.getSequenceBuilder()
                .add(new PrintAction())
                .build()
                .execute(context);

        ActionExecutionResult result = (ActionExecutionResult) context.getResults().get(0);
        String toString = result.toString();
        
        assertTrue(toString.contains("executionTimeMs"), 
                "toString should include execution time");
        assertTrue(toString.contains("PRINT_TASK"), 
                "toString should include action name");
    }
}

