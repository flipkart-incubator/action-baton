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
package com.flipkart.actionbaton.executor.builder;

import com.flipkart.actionbaton.entities.IAction;
import com.flipkart.actionbaton.entities.executor.RetryExecutorProperties;

/**
 * Builder for creating retry action executors.
 */
public interface IRetryActionBuilder extends IActionBuilder {
    /**
     * Sets the action to be executed with retries.
     *
     * @param action The action.
     * @return This builder instance.
     */
    IRetryActionBuilder action(IAction<?, ?> action);

    /**
     * Sets the retry configuration properties.
     *
     * @param properties The retry properties.
     * @return This builder instance.
     */
    IRetryActionBuilder properties(RetryExecutorProperties properties);
}

