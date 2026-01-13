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
 * Interface for conditional logic evaluation in the ActionBaton framework.
 * Predicates are used to decide whether to execute certain branches of a workflow.
 *
 * @param <T> The type of object (usually {@link ActionExecutionContext}) to verify.
 * @param <TEX> The type of exception that the predicate may throw.
 */
public interface IPredicate<T, TEX extends Exception> {
    /**
     * Verifies the condition based on the provided context.
     *
     * @param context The context to evaluate against.
     * @return {@code true} if the condition is met, {@code false} otherwise.
     * @throws TEX if an error occurs during verification.
     */
    boolean verify(T context) throws TEX;

    /**
     * Returns the unique name of the predicate.
     *
     * @return The predicate name.
     */
    String getPredicateName();
}
