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
 * Represents the lifecycle states of an action execution.
 */
public enum ActionStatus {
    /**
     * Action is waiting to be executed.
     */
    PENDING,

    /**
     * Action execution has just begun.
     */
    STARTED,

    /**
     * Action is currently executing its core logic.
     */
    IN_PROGRESS,

    /**
     * Action has finished successfully.
     */
    COMPLETED,

    /**
     * Action execution failed due to an error.
     */
    FAILED
}
