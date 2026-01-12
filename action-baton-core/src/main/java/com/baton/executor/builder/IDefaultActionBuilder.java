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
package com.baton.executor.builder;

import com.baton.entities.IAction;
import com.baton.exception.ActionBatonException;

/**
 * Builder for creating a default (single) action executor.
 * This is typically used to wrap a simple action into the {@link com.baton.executor.IActionBaton} interface.
 */
public interface IDefaultActionBuilder extends IActionBuilder {
    /**
     * Sets the single action to be executed.
     *
     * @param action The {@link IAction} instance.
     * @return The builder instance for chaining.
     * @throws ActionBatonException if the action is invalid.
     */
    IDefaultActionBuilder add(IAction action) throws ActionBatonException;
}
