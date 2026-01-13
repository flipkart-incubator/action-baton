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

import com.flipkart.actionbaton.entities.IMapper;
import com.flipkart.actionbaton.entities.actiondag.ActionDagEntity;
import com.flipkart.actionbaton.orchestrator.IActionOrchestrator;

/**
 * Builder for creating an executor that can run a Directed Acyclic Graph (DAG) of actions.
 * A DAG executor can resolve actions dynamically using an orchestrator or run a predefined DAG entity.
 */
public interface IActionDagExecutorBuilder extends IActionBuilder {
    /**
     * Sets a mapper to dynamically resolve the {@link ActionDagEntity} at runtime.
     *
     * @param mapper The mapper that returns an {@link ActionDagEntity}.
     * @return The builder instance for chaining.
     */
    IActionDagExecutorBuilder dynamicActionDag(IMapper mapper);

    /**
     * Sets a static {@link ActionDagEntity} to be executed.
     *
     * @param actionDagEntity The DAG entity defining the workflow.
     * @return The builder instance for chaining.
     */
    IActionDagExecutorBuilder actionDag(ActionDagEntity actionDagEntity);

    /**
     * Sets the orchestrator used to resolve action names and patterns within the DAG.
     *
     * @param actionOrchestrator The orchestrator instance.
     * @return The builder instance for chaining.
     */
    IActionDagExecutorBuilder orchestrator(IActionOrchestrator actionOrchestrator);
}
