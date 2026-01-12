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

import com.baton.entities.IIterator;
import com.baton.entities.IPredicate;
import com.baton.entities.executor.ParallelExecutorProperties;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.iterator.RandomIntIterator;
import com.baton.examples.predicate.EvenPredicate;
import com.baton.exception.ActionBatonException;
import org.junit.jupiter.api.Test;

public class MixedTests extends BaseActionBatonTests {
    @Test
    public void  MixedTests_Success() throws ActionBatonException {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("MIXED_FLOW");
        try {
            ParallelExecutorProperties properties = new ParallelExecutorProperties();
            properties.setThreadCount(10);
            IPredicate predicate = new EvenPredicate();
            IIterator iterator = new RandomIntIterator();

            actionBaton.getSequenceBuilder()
                    //Parallel Task Execution
                    .add(actionBaton.getParallelBuilder().add(actionList2).properties(properties).build())
                    //Sequential Task Execution
                    .add(actionBaton.getSequenceBuilder().add(actionList1).add(
                            //Conditional Task Execution
                            actionBaton.getConditionalBuilder()
                                    .predicate(predicate)
                                    .ifThen(printEvenAction)
                                    .elseThen(printAction)
                                    .build()
                    ).add(actionBaton.getIteratorActionBuilder().iterate(iterator).add(this.printIntegerAction).build())
                            .build())
                    .build().execute(context);
        } catch (ActionBatonException ex) {
                throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
