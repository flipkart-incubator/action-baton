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
package com.flipkart.actionbaton.tests.actions;

import com.flipkart.actionbaton.entities.executor.ParallelExecutorProperties;
import com.flipkart.actionbaton.examples.ActionExecutionContextImpl;
import com.flipkart.actionbaton.examples.CustomException;
import com.flipkart.actionbaton.examples.actions.ExecptionAction;
import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.executor.builder.IParallelActionBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParallelActionBatonTests extends BaseActionBatonTests {
    @Test
    public void  ParallelActionBatonTests() throws ActionBatonException {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("PARALLEL_FLOW");
        IParallelActionBuilder actionBuilder2 = actionBaton.getParallelBuilder();
        try {
            ParallelExecutorProperties properties = new ParallelExecutorProperties();
            properties.setThreadCount(10);
            actionBuilder2.add(actionList2).properties(properties).build().execute(context);
            Assertions.assertEquals(actionList2.size(), context.getResults().size());
        } catch (ActionBatonException ex) {
                throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(0, context.getFailedActions().size());
        Assertions.assertEquals(actionList2.size(), context.getResults().size());
    }

    @Test
    public void  ParallelActionBaton_FailedAction_Tests() throws ActionBatonException {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("PARALLEL_FLOW");
        IParallelActionBuilder actionBuilder2 = actionBaton.getParallelBuilder();
        boolean success = true;
        try {
            ParallelExecutorProperties properties = new ParallelExecutorProperties();
            properties.setThreadCount(10);
            actionList2.add(new ExecptionAction());
            actionBuilder2.add(actionList2).properties(properties).build().execute(context);
            success = false;
        } catch (ActionBatonException ex) {
            throw ex;
        } catch (CustomException e) {
            System.out.println("Exception Value : " + e.value);
        }
         catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(1, context.getFailedActions().size());
        Assertions.assertEquals(actionList2.size(), context.getResults().size());
        Assertions.assertTrue(success);
    }
}
