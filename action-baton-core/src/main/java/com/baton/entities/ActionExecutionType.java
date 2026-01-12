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
 * Defines the various types of execution patterns supported by the ActionBaton framework.
 */
public enum ActionExecutionType {
    /**
     * Default single action execution.
     */
    DEFAULT,

    /**
     * Sequential execution of a list of actions.
     */
    SEQUENTIAL,

    /**
     * Parallel execution of a list of actions.
     */
    PARALLEL,

    /**
     * Conditional execution based on a predicate.
     */
    CONDITIONAL,

    /**
     * Execution involving input/output data transformation via a mapper.
     */
    MAPPER,

    /**
     * Execution iterating over a collection of items.
     */
    ITERATOR,

    /**
     * Complex Directed Acyclic Graph (DAG) based execution.
     */
    ACTION_DAG
}
