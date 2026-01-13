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

import com.flipkart.actionbaton.entities.ActionExecutionContext;
import com.flipkart.actionbaton.entities.IPredicate;
import com.flipkart.actionbaton.examples.ActionExecutionContextImpl;

import java.util.Map;

/**
 * Predicate that returns true if status is not "COMPLETE".
 */
public class StatusNotCompletePredicate implements IPredicate<ActionExecutionContextImpl, Exception> {
    private final String statusKey;

    public StatusNotCompletePredicate() {
        this("status");
    }

    public StatusNotCompletePredicate(String statusKey) {
        this.statusKey = statusKey;
    }

    @Override
    public boolean verify(ActionExecutionContextImpl context) throws Exception {
        Map<String, Object> ctx = context.getContext();
        String status = (String) ctx.get(statusKey);
        return status == null || !"COMPLETE".equals(status);
    }

    @Override
    public String getPredicateName() {
        return "STATUS_NOT_COMPLETE_PREDICATE";
    }
}

