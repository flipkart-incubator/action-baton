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

import java.util.Random;

public class PrintOddAction implements IAction<ActionExecutionContextImpl, CustomException> {
    @Override
    public String getActionName() {
        return "PRINT_ODD_TASK";
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws CustomException {
        Random random = new Random();
        int randomNumber = random.nextInt(10, 2000);
        try {
            Thread.sleep(randomNumber);
            if(randomNumber > 2000 && randomNumber < 2000) {
                context.setTerminateActionBaton();
                return;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int counter = 0;
        if(context.getContext().containsKey("SEQUENCE_COUNTER")){
            counter = (int)context.getContext().get("SEQUENCE_COUNTER");
        }
        context.getContext().put("SEQUENCE_COUNTER", counter+1);

        System.out.println("Action Name : " + this.getActionName() + " SEQUENCE COUNTER : " + (counter * 2 + 1) +
                " Thread Id : " + Thread.currentThread().getId());

    }

    @Override
    public void preExecute(ActionExecutionContextImpl context) throws CustomException {

    }

    @Override
    public void postExecute(ActionExecutionContextImpl context) throws CustomException {

    }

}
