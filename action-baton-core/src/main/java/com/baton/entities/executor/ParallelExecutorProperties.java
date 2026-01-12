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
package com.baton.entities.executor;


import com.baton.commons.utils.ActionBatonConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration properties for parallel action execution.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParallelExecutorProperties {
    /**
     * Optional short title to describe this parallel executor configuration (for docs/metadata).
     */
    private String summary;

    /**
     * Optional detailed description of the parallel executor configuration (for docs/metadata).
     */
    private String description;

    /**
     * Maximum time to wait for all submitted tasks to complete, in milliseconds.
     */
    private int timeout = ActionBatonConstants.DEFAULT_PARALLEL_EXECUTOR_TIMEOUT;

    /**
     * Number of worker threads when using platform thread pools.
     */
    private int threadCount = ActionBatonConstants.DEFAULT_PARALLEL_EXECUTOR_THREADS;;

    /**
     * Use virtual threads for task execution.
     * Note: This flag is applicable on JDK 21+. On lower JDK versions it is ignored
     * and execution defaults to platform thread pools.
     */
    private boolean useVirtualThreads = false;
}
