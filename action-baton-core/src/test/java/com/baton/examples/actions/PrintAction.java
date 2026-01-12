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
package com.baton.examples.actions;

import com.baton.entities.ActionExecutionResult;
import com.baton.entities.ActionStatus;
import com.baton.entities.IAction;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.CustomException;
import com.baton.exception.ActionBatonException;

public class PrintAction implements IAction<ActionExecutionContextImpl, CustomException> {
    @Override
    public String getActionName() {
        return "PRINT_TASK";
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws CustomException {
        int counter = 0;
        if(context.getContext().containsKey("SEQUENCE_COUNTER")){
            counter = (int)context.getContext().get("SEQUENCE_COUNTER");
        }

        System.out.println("Action Name : " + this.getActionName() + " SEQUENCE COUNTER : " + counter +
                " Thread Id : " + Thread.currentThread().getId());
        context.getContext().put("SEQUENCE_COUNTER", counter+1);
    }

    @Override
    public void preExecute(ActionExecutionContextImpl context) throws CustomException {

    }

    @Override
    public void postExecute(ActionExecutionContextImpl context) throws CustomException {

    }
}
