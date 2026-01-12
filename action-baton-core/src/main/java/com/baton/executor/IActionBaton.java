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
package com.baton.executor;

import com.baton.entities.IAction;

/**
 * Marker interface for complex actions that act as executors for other actions.
 * An {@code IActionBaton} is an {@link IAction} that orchestrates the execution 
 * of one or more nested actions according to a specific pattern (e.g., sequential, parallel).
 * 
 * Implementations include {@link com.baton.executor.impl.SequentialActionBaton} 
 * and {@link com.baton.executor.impl.ParallelActionBaton}.
 */
public interface IActionBaton extends IAction {
}
