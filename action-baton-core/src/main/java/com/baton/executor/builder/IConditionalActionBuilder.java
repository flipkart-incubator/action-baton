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
import com.baton.entities.IPredicate;
import com.baton.exception.ActionBatonException;

/**
 * Builder for creating a conditional action executor.
 * A conditional executor evaluates a predicate and executes either the 'if' or 'else' branch.
 */
public interface IConditionalActionBuilder extends IActionBuilder {
    /**
     * Sets the predicate to be evaluated at runtime.
     *
     * @param predicate The {@link IPredicate} instance.
     * @return The builder instance for chaining.
     */
    IConditionalActionBuilder predicate(IPredicate predicate);

    /**
     * Sets the action to be executed if the predicate evaluates to {@code true}.
     *
     * @param action The action for the 'if' branch.
     * @return The builder instance for chaining.
     * @throws ActionBatonException if the action is invalid.
     */
    IConditionalActionBuilder ifThen(IAction action) throws ActionBatonException;

    /**
     * Sets the action to be executed if the predicate evaluates to {@code false}.
     *
     * @param action The action for the 'else' branch.
     * @return The builder instance for chaining.
     * @throws ActionBatonException if the action is invalid.
     */
    IConditionalActionBuilder elseThen(IAction action) throws ActionBatonException;
}
