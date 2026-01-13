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
package com.flipkart.actionbaton.commons.utils;

/**
 * Utility class containing constants and helper methods for generating internal keys.
 */
public class ActionBatonConstants {
    /**
     * Default timeout for parallel execution in milliseconds (30 seconds).
     */
    public static int DEFAULT_PARALLEL_EXECUTOR_TIMEOUT = 30000;

    /**
     * Default number of threads for parallel execution.
     */
    public static int DEFAULT_PARALLEL_EXECUTOR_THREADS = 1;

    /**
     * Generates a unique key for a mapper based on the action name and the current thread ID.
     *
     * @param actionName The name of the action.
     * @return The unique mapper key.
     */
    public static String getMapperKey(String actionName){
        return actionName + "-" + "MAPPER" + "-" + Thread.currentThread().getId();
    }

    /**
     * Generates a unique key for an input mapper based on the action name and the current thread ID.
     *
     * @param actionName The name of the action.
     * @return The unique input mapper key.
     */
    public static String getInputMapperKey(String actionName){
        return actionName + "-" + "MAPPER" + "-" + Thread.currentThread().getId() + "-INPUT";
    }

    /**
     * Generates a unique key for an output mapper based on the action name and the current thread ID.
     *
     * @param actionName The name of the action.
     * @return The unique output mapper key.
     */
    public static String getOutputMapperKey(String actionName){
        return actionName + "-" + "MAPPER" + "-" + Thread.currentThread().getId() + "-OUTPUT";
    }

    /**
     * Returns the constant key used for storing iterator items in the local context.
     *
     * @return The iterator key.
     */
    public static String getIteratorKey(){
        return "ITERATOR_KEY";
    }
}
