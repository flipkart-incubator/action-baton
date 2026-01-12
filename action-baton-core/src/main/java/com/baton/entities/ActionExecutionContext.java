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

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central state management class for a workflow execution.
 * It carries shared data, local temporary data, and execution results throughout the lifecycle of a workflow.
 *
 * @param <T1> The type of the workflow ID.
 * @param <T2> The type of the main shared context.
 */
@Getter
public abstract class ActionExecutionContext<T1, T2> {
    /**
     * The unique identifier for this workflow execution.
     */
    private final T1 workflowId;

    /**
     * The main shared data context accessible by all actions.
     */
    protected final T2 context;

    /**
     * A map for storing temporary, local data during the execution of specific actions or executors.
     * This context is thread-safe.
     */
    private final Map<String, Object> localContext;

    /**
     * A list of all results from individual action executions.
     */
    private final List<ActionExecutionResult> results;

    /**
     * A flag indicating whether the entire workflow execution should be terminated immediately.
     */
    private volatile boolean terminateActionBaton = false;

    /**
     * Creates a new execution context with a random UUID as the workflow ID.
     *
     * @param context The main shared context.
     */
    public ActionExecutionContext(T2 context) {
        this((T1) UUID.randomUUID().toString(), context);
    }

    /**
     * Creates a new execution context with a specified workflow ID.
     *
     * @param workflowId The unique workflow ID.
     * @param context The main shared context.
     */
    public ActionExecutionContext(T1 workflowId, T2 context){
        this.workflowId = workflowId;
        this.context = context;
        this.localContext = new ConcurrentHashMap<>();
        this.results = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Signals that the entire workflow should be terminated.
     */
    public void setTerminateActionBaton(){
        this.terminateActionBaton = true;
    }

    /**
     * Returns the main shared context.
     *
     * @return The context object.
     */
    public abstract T2 getContext();

    /**
     * Returns a list of all failed action execution results.
     *
     * @return A list of failed results.
     */
    public List<ActionExecutionResult> getFailedActions() {
        return this.results.stream().filter(x -> x.getStatus() == ActionStatus.FAILED).toList();
    }

}
