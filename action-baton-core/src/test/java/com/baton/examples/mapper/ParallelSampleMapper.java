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
import com.baton.exception.ActionBatonException;

import java.util.Map;
import java.util.UUID;

public class ParallelSampleMapper implements IMapper<Map<String,Object>, SampleMapper.SampleObject, ActionBatonException> {
    @Override
    public SampleMapper.SampleObject mapInput(Map<String, Object> context) {
        String random = UUID.randomUUID().toString();
        return new SampleMapper.SampleObject("A-" + random, "B-" + random, "C-" + random, "D-" + random);
    }

    @Override
    public String getMapperName() {
        return "ParallelSampleMapper";
    }
}
