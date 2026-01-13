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

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents the result of an individual action execution.
 * It captures the action itself, its final status, any error encountered, and timing information.
 */
@Getter
public class ActionExecutionResult {
    /**
     * The action that was executed.
     */
    private IAction action;

    /**
     * The final status of the action execution.
     */
    private ActionStatus status;

    /**
     * The exception thrown during execution, if any.
     */
    private Exception errorException;

    /**
     * The time when the action execution started.
     */
    private Instant startTime;

    /**
     * The time when the action execution ended.
     */
    private Instant endTime;

    /**
     * Creates a result with just action and status.
     *
     * @param action The executed action.
     * @param status The final status.
     */
    public ActionExecutionResult(IAction action, ActionStatus status){
        this.action = action;
        this.status = status;
    }

    /**
     * Creates a result with an error.
     *
     * @param action The executed action.
     * @param status The final status.
     * @param errorException The exception encountered.
     */
    public ActionExecutionResult(IAction action, ActionStatus status, Exception errorException){
        this.action = action;
        this.status = status;
        this.errorException = errorException;
    }

    /**
     * Creates a result with full timing information.
     *
     * @param action The executed action.
     * @param status The final status.
     * @param errorException The exception encountered.
     * @param startTime The start time.
     * @param endTime The end time.
     */
    public ActionExecutionResult(IAction action, ActionStatus status, Exception errorException, 
                                 Instant startTime, Instant endTime){
        this.action = action;
        this.status = status;
        this.errorException = errorException;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Computes and returns the total execution time in milliseconds.
     *
     * @return The execution duration in milliseconds, or {@code null} if timing information is missing.
     */
    public Long getExecutionTimeMs() {
        if (startTime == null || endTime == null) {
            return null;
        }
        return Duration.between(startTime, endTime).toMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (action != null) {
            sb.append("action=").append(action.getActionName());
        }
        if (status != null) {
            sb.append(", status=").append(status);
        }
        if (errorException != null) {
            sb.append(", errorException=").append(errorException.getMessage());
        }
        Long time = getExecutionTimeMs();
        if (time != null) {
            sb.append(", executionTimeMs=").append(time);
        }
        sb.append("}");
        return sb.toString();
    }
}
