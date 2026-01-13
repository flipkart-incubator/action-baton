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
package com.flipkart.actionbaton.entities;

/**
 * Base interface for all executable actions in the ActionBaton framework.
 * Actions are the fundamental units of work that can be orchestrated.
 *
 * @param <T> The type of {@link ActionExecutionContext} used by the action.
 * @param <TEX> The type of exception that the action may throw.
 */
public interface IAction<T extends ActionExecutionContext, TEX extends Exception> {
    /**
     * Returns the unique name of the action.
     *
     * @return The action name.
     */
    String getActionName();

    /**
     * Executes the main business logic of the action.
     *
     * @param context The execution context containing shared and local state.
     * @throws TEX if an error occurs during execution.
     */
    void execute(T context) throws TEX;

    /**
     * Hook method called before {@link #execute(ActionExecutionContext)}.
     * Can be used for initialization or setup.
     *
     * @param context The execution context.
     * @throws TEX if an error occurs during pre-execution.
     */
    default void preExecute(T context) throws TEX {

    }

    /**
     * Hook method called after {@link #execute(ActionExecutionContext)}.
     * Can be used for cleanup or finalization.
     *
     * @param context The execution context.
     * @throws TEX if an error occurs during post-execution.
     */
    default void postExecute(T context) throws TEX {}

    /**
     * Determines whether the action should be executed.
     * If this returns {@code false}, {@link #execute(ActionExecutionContext)} will be skipped.
     *
     * @param context The execution context.
     * @return {@code true} if the action should execute, {@code false} otherwise.
     * @throws TEX if an error occurs during the check.
     */
    default boolean doExecute(T context) throws TEX{
        return true;
    }
}
