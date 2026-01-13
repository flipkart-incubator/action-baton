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

import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.executor.IActionBaton;

/**
 * Base interface for all action builders in the framework.
 * Provides a standard {@link #build()} method to create the final {@link IActionBaton}.
 */
public interface IActionBuilder {
    /**
     * Finalizes the configuration and creates the {@link IActionBaton} instance.
     *
     * @return The constructed action baton.
     * @throws ActionBatonException if the configuration is invalid or incomplete.
     */
    IActionBaton build() throws ActionBatonException;
}


