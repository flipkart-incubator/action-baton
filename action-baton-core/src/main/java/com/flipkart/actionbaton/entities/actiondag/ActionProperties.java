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
package com.flipkart.actionbaton.entities.actiondag;

import com.flipkart.actionbaton.entities.executor.ConditionalExecutorEntity;
import com.flipkart.actionbaton.entities.executor.IteratorExecutorProperties;
import com.flipkart.actionbaton.entities.executor.MapperExecutorProperties;
import com.flipkart.actionbaton.entities.executor.ParallelExecutorProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Container for various executor-specific configuration properties in an {@link ActionEntity}.
 */
@Getter
@Setter
@NoArgsConstructor
public class ActionProperties {
    /**
     * Properties for parallel execution.
     */
    private ParallelExecutorProperties parallel;

    /**
     * Properties for conditional execution.
     */
    private ConditionalExecutorEntity conditional;

    /**
     * Properties for mapper-based execution.
     */
    private MapperExecutorProperties mapper;

    /**
     * Properties for iterator-based execution.
     */
    private IteratorExecutorProperties iterator;
}



