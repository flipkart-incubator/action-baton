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
package com.baton.entities;

/**
 * Interface for data transformation before and after action execution.
 * Mappers allow for decoupling the action's logic from the shared execution context.
 *
 * @param <TIN> The type of the input (usually {@link ActionExecutionContext}).
 * @param <TOUT> The type of the transformed output object.
 * @param <TEX> The type of exception that the mapper may throw.
 */
public interface IMapper<TIN, TOUT, TEX extends Exception> {
    /**
     * Transforms data from the input context into a format suitable for the action.
     *
     * @param context The source context.
     * @return The transformed data object.
     * @throws TEX if an error occurs during input mapping.
     */
    TOUT mapInput(TIN context) throws TEX;

    /**
     * Optional hook to process or transform data back into the context after the action executes.
     *
     * @param context The target context.
     * @throws TEX if an error occurs during output mapping.
     */
    default void mapOutput(TIN context) throws TEX {
    }

    /**
     * Returns the unique name of the mapper.
     *
     * @return The mapper name.
     */
    String getMapperName();
}
