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

import com.baton.commons.utils.ActionBatonConstants;
import com.baton.entities.IAction;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.CustomException;
import com.baton.examples.mapper.SampleMapper;

public class PrintActionWithMapper implements IAction<ActionExecutionContextImpl, CustomException> {
    @Override
    public String getActionName() {
        return "PRINT_ACTION_WITH_MAPPER_TASK";
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws CustomException {
        String inputMapperKey = ActionBatonConstants.getInputMapperKey(getActionName());
        System.out.println("Input Mapper Key : " + inputMapperKey);

        SampleMapper.SampleObject input = (SampleMapper.SampleObject)context.getLocalContext().get(inputMapperKey);
        System.out.println("A : " + input.valueA + " | B: " + input.valueB + " | C: " + input.valueC + " | D: " + input.valueD);
        context.getLocalContext().put(ActionBatonConstants.getOutputMapperKey(getActionName()), "MAPPER ACTION SUCCESS");
    }

    @Override
    public void preExecute(ActionExecutionContextImpl context) throws CustomException {

    }

    @Override
    public void postExecute(ActionExecutionContextImpl context) throws CustomException {

    }
}
