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
package com.flipkart.actionbaton.examples.predicate;

import com.flipkart.actionbaton.entities.IPredicate;
import com.flipkart.actionbaton.examples.ActionExecutionContextImpl;
import com.flipkart.actionbaton.exception.ActionBatonException;

public class EvenPredicate implements IPredicate<ActionExecutionContextImpl, ActionBatonException> {
    @Override
    public boolean verify(ActionExecutionContextImpl context) {
        if(context.getContext().containsKey("SEQUENCE_COUNTER")){
            int counter = (int)context.getContext().get("SEQUENCE_COUNTER");
            return (counter % 2 == 0);
        }
        return false;
    }

    @Override
    public String getPredicateName() {
        return this.getClass().getName();
    }
}
