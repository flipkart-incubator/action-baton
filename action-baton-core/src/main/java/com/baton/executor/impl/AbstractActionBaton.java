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
package com.baton.executor.impl;

import com.baton.entities.*;
import com.baton.executor.IActionBaton;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * Base class for all action executors.
 * Provides standard lifecycle management and result registration for orchestrated actions.
 */
@Slf4j
public abstract class AbstractActionBaton implements IActionBaton {
    /**
     * The type of execution pattern this executor implements.
     */
    private final ActionExecutionType type;

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getActionName() {
        return this.type.toString();
    }

    /**
     * Constructs a new abstract action baton with the specified execution type.
     *
     * @param type The execution type.
     */
    protected AbstractActionBaton(ActionExecutionType type) {
        this.type = type;
    }

    /**
     * Executes a single action within the lifecycle managed by this executor.
     * Includes pre-execution, execution, post-execution, and result registration.
     *
     * @param context The execution context.
     * @param action The action to execute.
     * @throws Exception if any part of the execution lifecycle fails.
     */
    protected void executeAction(ActionExecutionContext context, IAction action) throws Exception {

        boolean exceptionThrown = false;
        ActionExecutionResult result = null;
        Instant startTime = Instant.now();

        try{
            if(action.doExecute(context)){
                try {
                    action.preExecute(context);
                    action.execute(context);

                    result = getResult(action, ActionStatus.COMPLETED, null, startTime);
                } catch (Exception e) {
                    exceptionThrown = true;
                    result = getResult(action, ActionStatus.FAILED, e, startTime);
                    throw e;
                } finally {
                    registerActionExecutionResult(context, action, result);
                    action.postExecute(context);
                }
            }
        } catch (Exception e) {
            if (!exceptionThrown) {
                result = getResult(action, ActionStatus.FAILED, e, startTime);
                registerActionExecutionResult(context, action, result);
            }
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void preExecute(ActionExecutionContext context) throws Exception {
        log.debug(String.format("ActionBaton Started : %s", context.getWorkflowId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postExecute(ActionExecutionContext context) throws Exception {
        log.debug(String.format("ActionBaton Completed : %s", context.getWorkflowId()));
    }

    /**
     * Registers the result of an action execution in the context.
     * Skips registration if the action is another {@link IActionBaton} to avoid duplicate results for nested flows.
     *
     * @param context The execution context.
     * @param action The executed action.
     * @param result The execution result.
     */
    private void registerActionExecutionResult(ActionExecutionContext context, IAction action, ActionExecutionResult result) {
        if (!(action instanceof IActionBaton)) {
            context.getResults().add(result);
        }
    }

    /**
     * Creates an {@link ActionExecutionResult} object.
     *
     * @param action The executed action.
     * @param status The final status of the execution.
     * @param ex The exception, if any.
     * @param startTime The start time of the execution.
     * @return The populated result object.
     */
    private ActionExecutionResult getResult(IAction action, ActionStatus status, Exception ex, Instant startTime) {
        Instant endTime = Instant.now();
        return new ActionExecutionResult(action, status, ex, startTime, endTime);
    }

}
