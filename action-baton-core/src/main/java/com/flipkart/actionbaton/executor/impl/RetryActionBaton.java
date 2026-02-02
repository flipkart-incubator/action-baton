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
package com.flipkart.actionbaton.executor.impl;

import com.flipkart.actionbaton.entities.ActionExecutionContext;
import com.flipkart.actionbaton.entities.ActionExecutionType;
import com.flipkart.actionbaton.entities.IAction;
import com.flipkart.actionbaton.entities.executor.RetryExecutorProperties;
import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.executor.IActionBaton;
import com.flipkart.actionbaton.executor.builder.IRetryActionBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Executor that runs an action with retry logic.
 */
@Slf4j
final class RetryActionBaton extends AbstractActionBaton {
    /**
     * The action to execute.
     */
    private final IAction<?, ?> action;

    /**
     * The retry configuration properties.
     */
    private final RetryExecutorProperties properties;

    /**
     * Constructs a new retry action baton.
     *
     * @param action The action to execute.
     * @param properties The retry properties.
     */
    RetryActionBaton(IAction<?, ?> action, RetryExecutorProperties properties) {
        super(ActionExecutionType.RETRY);
        this.action = action;
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"rawtypes"})
    public void execute(ActionExecutionContext context) throws Exception {
        int attempts = 0;
        int maxRetries = properties.getMaxRetries();
        long delay = properties.getRetryDelay();

        while (attempts <= maxRetries) {
            try {
                executeAction(context, this.action);
                return; // Success, exit
            } catch (Exception e) {
                attempts++;
                if (attempts > maxRetries) {
                    log.error(String.format("Action failed after %d retries: %s", maxRetries, e.getMessage()), e);
                    throw e;
                }
                log.warn(String.format("Action failed, retrying (attempt %d/%d) after %d ms: %s",
                        attempts, maxRetries, delay, e.getMessage()));
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            }
        }
    }
}

/**
 * Builder for creating {@link RetryActionBaton} instances.
 */
final class RetryActionBuilder implements IRetryActionBuilder {
    /**
     * Internal action being built.
     */
    private IAction<?, ?> action;

    /**
     * Internal retry properties being built.
     */
    private RetryExecutorProperties properties = new RetryExecutorProperties();

    /**
     * {@inheritDoc}
     */
    @Override
    public IRetryActionBuilder action(IAction<?, ?> action) {
        this.action = action;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRetryActionBuilder properties(RetryExecutorProperties properties) {
        if (properties != null) {
            this.properties = properties;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if (this.action == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Action is required for Retry Action");
        }
        return new RetryActionBaton(action, properties);
    }
}

