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
package com.flipkart.actionbaton.executor.builder;

import com.flipkart.actionbaton.entities.IAction;
import com.flipkart.actionbaton.entities.IMapper;

/**
 * Builder interface for creating mapper-based action executors.
 */
public interface IMapperActionBatonBuilder extends IActionBuilder {
    /**
     * Sets the mapper to use for data transformation.
     *
     * @param mapper The {@link IMapper} instance.
     * @return The builder instance.
     */
    IMapperActionBatonBuilder mapper(IMapper mapper);

    /**
     * Sets the action to execute within the mapped context.
     *
     * @param action The action to execute.
     * @return The builder instance.
     */
    IMapperActionBatonBuilder action(IAction action);
}
