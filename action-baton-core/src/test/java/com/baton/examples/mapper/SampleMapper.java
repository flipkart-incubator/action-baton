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
package com.baton.examples.mapper;

import com.baton.commons.utils.ActionBatonConstants;
import com.baton.entities.IMapper;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.exception.ActionBatonException;

import java.util.Map;

public class SampleMapper implements IMapper<ActionExecutionContextImpl, SampleMapper.SampleObject, ActionBatonException> {
    @Override
    public SampleObject mapInput(ActionExecutionContextImpl actionExecutionContext) {
        Map<String, Object> context = actionExecutionContext.getContext();
        return new SampleObject(context.get("VALUE_A").toString(),
                context.get("VALUE_B").toString(),
                context.get("VALUE_C").toString(),
                context.get("VALUE_D").toString());
    }

    @Override
    public void mapOutput(ActionExecutionContextImpl actionExecutionContext) {
        String mapperKey = ActionBatonConstants.getOutputMapperKey("PRINT_ACTION_WITH_MAPPER_TASK");
        System.out.println(actionExecutionContext.getLocalContext().get(mapperKey));
    }

    @Override
    public String getMapperName() {
        return "SampleMapper";
    }

    public static class SampleObject {
        public String valueA, valueB, valueC, valueD;
        public SampleObject(String valueA, String valueB, String valueC, String valueD) {
            this.valueA = valueA;
            this.valueB = valueB;
            this.valueC = valueC;
            this.valueD = valueD;
        }
    }
}


