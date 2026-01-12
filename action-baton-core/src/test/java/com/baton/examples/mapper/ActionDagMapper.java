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

import com.baton.entities.IMapper;
import com.baton.entities.actiondag.ActionDagEntity;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.StaticUtils;
import com.baton.exception.ActionBatonException;

import java.io.IOException;

public class ActionDagMapper implements IMapper<ActionExecutionContextImpl, ActionDagEntity, ActionBatonException> {
    @Override
    public ActionDagEntity mapInput(ActionExecutionContextImpl context) throws ActionBatonException {
        try {
            return StaticUtils.parseJSONFile("src/test/java/com/baton/tests/actiondags/SEQUENTIAL_TEST_1.json");
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public String getMapperName() {
        return "ACTION_DAG_MAPPER";
    }
}
