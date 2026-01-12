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
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.CustomException;
import com.baton.examples.iterator.RandomIntIterator;
import com.baton.exception.ActionBatonException;
import org.junit.jupiter.api.Test;

public class IteratorActionBatonTests  extends BaseActionBatonTests {

    @Test
    public void  IteratorActionBatonTests_If_Success() throws ActionBatonException, Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("ITERATOR_FLOW");

        IIterator iterator = new RandomIntIterator();

        try {
            actionBaton.getIteratorActionBuilder().iterate(iterator).add(this.printIntegerAction).build().execute(context);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
