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

import com.baton.entities.ActionExecutionResult;
import com.baton.entities.IAction;
import com.baton.entities.executor.ParallelExecutorProperties;
import com.baton.exception.ActionBatonException;
import com.baton.entities.ActionExecutionContext;
import com.baton.entities.ActionExecutionType;
import com.baton.executor.IActionBaton;
import com.baton.executor.builder.IParallelActionBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Executor that runs a list of actions in parallel using a configurable thread pool.
 */
@Slf4j
final class ParallelActionBaton extends AbstractActionBaton {
    /**
     * The list of actions to be executed in parallel.
     */
    private final List<IAction> actionList;

    /**
     * Configuration properties for parallel execution.
     */
    private final ParallelExecutorProperties properties;

    /**
     * Constructs a new parallel action baton.
     *
     * @param actionList The list of actions.
     * @param properties Configuration for the executor.
     */
    ParallelActionBaton(List<IAction> actionList, ParallelExecutorProperties properties) {
        super(ActionExecutionType.PARALLEL);
        this.actionList = actionList;
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        ExecutorService executorService = ExecutorServices.createExecutor(this.properties);
        try {
            for (IAction action : this.actionList) {
                executorService.submit(() -> {
                    this.executeAction(context, action);
                    return true;
                });
            }

            executorService.shutdown();
            if (!executorService.awaitTermination(this.properties.getTimeout(), TimeUnit.MILLISECONDS)) {
                log.error("Timeout occurred before all tasks could be completed.");
                throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_EXECUTION_FAILED, "Timeout occurred before all tasks could be completed.");
            }

            //Handle Any failed action, which need to propagate properly to callee.
            List<ActionExecutionResult> failedActions = context.getFailedActions();
            if (!failedActions.isEmpty()) {
                throw failedActions.get(0).getErrorException();
            }
        } catch (InterruptedException e) {
            log.error(String.format("Interrupted while executing action: %1$s", e.getMessage()), e);
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_EXECUTION_FAILED, e.getMessage());
        }
    }
}

/**
 * Builder for creating {@link ParallelActionBaton} instances.
 */
final class ParallelActionBuilder implements IParallelActionBuilder {
    /**
     * Internal list of actions being built.
     */
    private final List<IAction> actionList = new ArrayList<>();

    /**
     * Internal configuration properties.
     */
    private ParallelExecutorProperties properties;

    /**
     * {@inheritDoc}
     */
    @Override
    public IParallelActionBuilder add(List<IAction> actionList)  throws ActionBatonException {
        if(actionList.isEmpty()) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Empty Action List is not allowed");
        }
        this.actionList.addAll(actionList);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParallelActionBuilder add(IAction action)  throws ActionBatonException {
        if(action == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Nullable Action is not allowed");
        }
        this.actionList.add(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IParallelActionBuilder properties(ParallelExecutorProperties properties) throws ActionBatonException {
        if(properties.getThreadCount() <= 0) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Expected thread count should be Positive Integer");
        }
        this.properties = properties;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(actionList.isEmpty()) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "At least one Action is required for Parallel Action");

        if(this.properties == null){
            this.properties = new ParallelExecutorProperties();
            this.properties.setThreadCount(this.actionList.size());
        }

        return new ParallelActionBaton(this.actionList, this.properties);
    }
}
