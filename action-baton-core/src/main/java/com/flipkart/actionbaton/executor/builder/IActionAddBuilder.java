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
import com.flipkart.actionbaton.exception.ActionBatonException;

import java.util.List;

/**
 * Interface for builders that support adding actions to a collection.
 * This is used by executors that manage multiple actions, like sequential or parallel executors.
 *
 * @param <T> The type of the builder for fluent chaining.
 */
public interface IActionAddBuilder<T> {
    /**
     * Adds a list of actions to the builder.
     *
     * @param actionList The list of {@link IAction} instances to add.
     * @return The builder instance for chaining.
     * @throws ActionBatonException if any action is invalid.
     */
    T add(List<IAction> actionList) throws ActionBatonException;

    /**
     * Adds a single action to the builder.
     *
     * @param action The {@link IAction} instance to add.
     * @return The builder instance for chaining.
     * @throws ActionBatonException if the action is invalid.
     */
    T add(IAction action) throws ActionBatonException;

}
