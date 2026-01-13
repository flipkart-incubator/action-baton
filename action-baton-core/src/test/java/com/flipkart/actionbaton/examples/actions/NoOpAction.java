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
package com.flipkart.actionbaton.examples.actions;

import com.flipkart.actionbaton.entities.IAction;
import com.flipkart.actionbaton.examples.ActionExecutionContextImpl;

public class NoOpAction implements IAction<ActionExecutionContextImpl, Exception> {
    @Override
    public String getActionName() {
        return "NO_OP";
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws Exception {
        // intentionally left blank
    }
}


