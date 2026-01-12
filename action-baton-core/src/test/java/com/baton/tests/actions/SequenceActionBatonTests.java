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

import com.baton.entities.IAction;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.CustomException;
import com.baton.executor.builder.ISequenceActionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SequenceActionBatonTests extends BaseActionBatonTests {
    @Test
    public void SequenceActionTests_WithoutSequenceCounter_Success() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_1");
        ISequenceActionBuilder actionsBuilder = actionBaton.getSequenceBuilder();
        try {
            actionsBuilder.add(actionList1).build().execute(context);
        } catch (CustomException cex) {
            throw cex;
        }
         catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(actionList1.size(), context.getContext().get("SEQUENCE_COUNTER"));
        Assertions.assertEquals(actionList1.size(), context.getResults().size());

    }

    @Test
    public void SequenceActionTests_WithSequenceCounter_Success() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_2");
        int counter = 5;
        context.getContext().put("SEQUENCE_COUNTER", counter);

        ISequenceActionBuilder actionsBuilder = actionBaton.getSequenceBuilder();
        try {
            actionsBuilder.add(actionList1).build().execute(context);
        } catch (CustomException cex) {
            throw cex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(actionList1.size(), context.getResults().size());

        Assertions.assertEquals(counter + actionList1.size(), context.getContext().get("SEQUENCE_COUNTER"));

    }

    @Test
    public void SequenceActionTests_WithException() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW__EXCEPTION_1");
        List<IAction> actions = Arrays.asList(this.exceptionAction);
        ISequenceActionBuilder actionsBuilder = actionBaton.getSequenceBuilder();

        Assertions.assertThrows(CustomException.class, () -> actionsBuilder.add(actions).build().execute(context));
        context.getFailedActions().forEach(action -> {
            Assertions.assertEquals("EXCEPTION_ACTION", action.getAction().getActionName());
        });
    }

    @Test
    public void SequencePost_ExecuteActionTests_WithException() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW__EXCEPTION_2");
        List<IAction> actions = Arrays.asList(this.postExecuteExceptionAction);
        ISequenceActionBuilder actionsBuilder = actionBaton.getSequenceBuilder();

        Assertions.assertThrows(CustomException.class, () -> actionsBuilder.add(actions).build().execute(context));
        context.getFailedActions().forEach(action -> {
            Assertions.assertEquals("POST_EXECUTE_EXCEPTION_ACTION", action.getAction().getActionName());
        });
    }

    @Test
    public void SequenceDo_ExecuteActionTests_WithException() throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW__EXCEPTION_3");
        List<IAction> actions = Arrays.asList(this.doExecuteExceptionAction);
        ISequenceActionBuilder actionsBuilder = actionBaton.getSequenceBuilder();

        Assertions.assertThrows(CustomException.class, () -> actionsBuilder.add(actions).build().execute(context));
        context.getFailedActions().forEach(action -> {
            Assertions.assertEquals("DO_EXECUTE_EXCEPTION_ACTION", action.getAction().getActionName());
        });
    }
}
