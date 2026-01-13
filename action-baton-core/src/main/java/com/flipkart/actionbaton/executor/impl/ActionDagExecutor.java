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
import com.flipkart.actionbaton.entities.IMapper;
import com.flipkart.actionbaton.entities.actiondag.ActionDagEntity;
import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.executor.IActionBaton;
import com.flipkart.actionbaton.executor.builder.IActionDagExecutorBuilder;
import com.flipkart.actionbaton.orchestrator.IActionOrchestrator;

/**
 * Executor that executes a complex Directed Acyclic Graph (DAG) of actions.
 * The DAG can be either provided statically or resolved dynamically at runtime using a mapper.
 */
public class ActionDagExecutor extends AbstractActionBaton {
    /**
     * The static DAG entity to execute.
     */
    private final ActionDagEntity actionDagEntity;

    /**
     * The orchestrator used to resolve and execute actions within the DAG.
     */
    private final IActionOrchestrator actionOrchestrator;

    /**
     * An optional mapper to dynamically resolve the DAG entity based on the execution context.
     */
    private final IMapper dynamicActionDagMapper;

    /**
     * Constructs a new Action DAG executor.
     *
     * @param actionOrchestrator The orchestrator.
     * @param actionDagEntity The static DAG (can be null if dynamic mapper is provided).
     * @param dynamicActionDagMapper The dynamic DAG mapper (can be null if static DAG is provided).
     */
    protected ActionDagExecutor(IActionOrchestrator actionOrchestrator, ActionDagEntity actionDagEntity,
                                IMapper dynamicActionDagMapper) {
        super(ActionExecutionType.ACTION_DAG);

        this.actionDagEntity = actionDagEntity;
        this.actionOrchestrator = actionOrchestrator;
        this.dynamicActionDagMapper = dynamicActionDagMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        ActionDagEntity actionDag = this.actionDagEntity;
        if (actionDag == null) {
            actionDag = (ActionDagEntity) dynamicActionDagMapper.mapInput(context);
        }

        this.actionOrchestrator.executeActionDag(context, actionDag);
    }
}

/**
 * Builder for creating {@link ActionDagExecutor} instances.
 */
final class ActionDagExecuteBuilder implements IActionDagExecutorBuilder {
    /**
     * Internal static DAG entity.
     */
    private ActionDagEntity actionDagEntity;

    /**
     * Internal orchestrator.
     */
    private IActionOrchestrator actionOrchestrator;

    /**
     * Internal dynamic DAG mapper.
     */
    private IMapper dynamicActionDagMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionDagExecutorBuilder dynamicActionDag(IMapper dynamicActionDagMapper) {
        this.dynamicActionDagMapper = dynamicActionDagMapper;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionDagExecutorBuilder actionDag(ActionDagEntity actionDagEntity) {
        this.actionDagEntity = actionDagEntity;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionDagExecutorBuilder orchestrator(IActionOrchestrator actionOrchestrator) {
        this.actionOrchestrator = actionOrchestrator;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(actionDagEntity == null && dynamicActionDagMapper == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Either Action Dag Entity or Dynamic ActionDag Mapper is required for Action Dag execution");
        }
        if(actionOrchestrator == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Action orchestrator is required for Action Dag execution");
        }

        return new ActionDagExecutor(this.actionOrchestrator, this.actionDagEntity, this.dynamicActionDagMapper);
    }
}
