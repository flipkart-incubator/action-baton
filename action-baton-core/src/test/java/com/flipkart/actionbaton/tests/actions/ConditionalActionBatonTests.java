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

import com.flipkart.actionbaton.entities.IPredicate;
import com.flipkart.actionbaton.examples.ActionExecutionContextImpl;
import com.flipkart.actionbaton.examples.CustomException;
import com.flipkart.actionbaton.examples.predicate.EvenPredicate;
import com.flipkart.actionbaton.exception.ActionBatonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionalActionBatonTests extends BaseActionBatonTests
{
    @Test
    public void  ConditionalActionBatonTests_If_Success() throws ActionBatonException, Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("CONDITIONAL_FLOW_IF");
        context.getContext().put("SEQUENCE_COUNTER", 10);
        IPredicate predicate = new EvenPredicate();

        try {
            actionBaton.getConditionalBuilder()
                    .predicate(predicate)
                    .ifThen(printEvenAction)
                    .elseThen(printAction)
                    .build().execute(context);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(11, context.getContext().get("SEQUENCE_COUNTER"));
    }
    @Test
    public void  ConditionalActionBatonTests_Else_Success() throws ActionBatonException, Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("CONDITIONAL_FLOW_ELSE");
        context.getContext().put("SEQUENCE_COUNTER", 1);
        IPredicate predicate = new EvenPredicate();
        try {
            actionBaton.getConditionalBuilder()
                    .predicate(predicate)
                    .ifThen(printEvenAction)
                    .elseThen(printAction)
                    .build().execute(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(2, context.getContext().get("SEQUENCE_COUNTER"));
    }

}
