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

import com.baton.entities.IMapper;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.actions.PrintActionWithMapper;
import com.baton.examples.mapper.SampleMapper;
import com.baton.exception.ActionBatonException;
import org.junit.jupiter.api.Test;

public class MapperActionBatonTests extends BaseActionBatonTests {

    @Test
    public void ActionBatonWithMapperTests_Success() throws ActionBatonException {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("WITH_MAPPER");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");

        IMapper mapper = new SampleMapper();
        try {
            actionBaton.getMapperExecutor().mapper(mapper).action(new PrintActionWithMapper()).build().execute(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
