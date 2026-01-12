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
package com.baton.orchestrator.impl;

import com.baton.entities.ActionExecutionContext;
import com.baton.entities.IAction;
import com.baton.entities.IMapper;
import com.baton.entities.actiondag.ActionDagEntity;
import com.baton.entities.actiondag.ActionEntity;
import com.baton.entities.actiondag.ActionProperties;
import com.baton.entities.executor.ConditionalExecutorEntity;
import com.baton.entities.executor.IteratorExecutorProperties;
import com.baton.entities.executor.MapperExecutorProperties;
import com.baton.exception.ActionBatonException;
import com.baton.executor.builder.*;
import com.baton.executor.impl.ActionBatonBuilderFactory;
import com.baton.orchestrator.IActionOrchestrator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of {@link IActionOrchestrator} that handles JSON parsing and DAG execution.
 * Subclasses must implement the name-to-implementation resolution methods.
 */
@Slf4j
public abstract class BaseActionOrchestrator implements IActionOrchestrator {

    /**
     * The Jackson object mapper for JSON processing.
     */
    private final ObjectMapper objectMapper;

    /**
     * The factory for creating action executors.
     */
    private final ActionBatonBuilderFactory actionBaton;

    /**
     * Initializes the orchestrator with a default object mapper and the singleton builder factory.
     */
    public BaseActionOrchestrator() {
        objectMapper = new ObjectMapper();
        actionBaton = ActionBatonBuilderFactory.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void executeActionDag(ActionExecutionContext context, ActionDagEntity actionDagEntity) throws Exception {
        if(actionDagEntity == null || actionDagEntity.getRootAction() == null) {
            assert actionDagEntity != null;
            throw new ActionBatonException(ActionBatonException.ErrorCode.INVALID_ACTION_DAG_ENTITY, actionDagEntity.getActionDagId());
        }

        execute(actionDagEntity.getRootAction()).execute(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void executeActionDag(ActionExecutionContext context, String actionDag)
            throws Exception {
        try {
            ActionDagEntity entity = objectMapper.readValue(actionDag, ActionDagEntity.class);
            executeActionDag(context, entity);
        } catch (JsonProcessingException e) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.INVALID_ACTION_DAG_ENTITY);
        }
    }

    /**
     * Recursively builds the action executor tree from the DAG configuration.
     *
     * @param actionEntity The root entity of the (sub)tree.
     * @return The built {@link IAction} instance.
     * @throws ActionBatonException if the configuration is invalid.
     */
    private IAction execute(ActionEntity actionEntity) throws ActionBatonException {
        if(actionEntity == null) {
            return null;
        }

        List<IAction> actionList = new ArrayList<>();
        if(actionEntity.getActions() != null) {
            for (ActionEntity action : actionEntity.getActions()) {
                IAction childAction = execute(action);
                if (childAction != null) {
                    actionList.add(childAction);
                }
            }
        }
        return getAction(actionEntity, actionList);
    }

    /**
     * Resolves the appropriate executor based on the execution type in the entity.
     *
     * @param actionEntity The action entity.
     * @param actions The list of pre-built child actions.
     * @return The resolved executor.
     * @throws ActionBatonException if the execution type is unsupported.
     */
    private IAction getAction(ActionEntity actionEntity, List<IAction> actions) throws ActionBatonException {
        return switch (actionEntity.getExecutor()) {
            case DEFAULT -> getDefaultAction(actionEntity);
            case SEQUENTIAL -> this.actionBaton.getSequenceBuilder().add(actions).build();
            case PARALLEL -> getParallelAction(actionEntity, actions);
            case CONDITIONAL -> getConditionalAction(actionEntity, actions);
            case MAPPER -> getMapperAction(actionEntity);
            case ITERATOR -> getIteratorAction(actionEntity, actions);
            case ACTION_DAG -> getActionDagActionBaton(actionEntity, actions);
            default -> throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED);
        };
    }

    /**
     * Builds a default action (single execution).
     *
     * @param actionEntity The action entity.
     * @return The built action.
     * @throws ActionBatonException if building fails.
     */
    private IAction getDefaultAction(ActionEntity actionEntity) throws ActionBatonException {
        IAction underlyingAction;
        
        if(isNullOrEmpty(actionEntity.getMapperName())) {
            underlyingAction = getAction(actionEntity.getName());
        } else {
            ActionProperties properties = new ActionProperties();
            properties.setMapper(new MapperExecutorProperties(actionEntity.getMapperName()));
            actionEntity.setProperties(properties);
            underlyingAction = getMapperAction(actionEntity);
        }
        
        // Create DefaultActionBaton with the action
        return this.actionBaton.getDefaultActionBuilder().add(underlyingAction).build();
    }

    /**
     * Builds a parallel action executor.
     *
     * @param actionEntity The action entity.
     * @param actions The child actions.
     * @return The parallel action.
     * @throws ActionBatonException if configuration is invalid.
     */
    private IAction getParallelAction(ActionEntity actionEntity, List<IAction> actions) throws ActionBatonException {
        if(actions.isEmpty()) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED,
                    actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        IParallelActionBuilder actionBuilder = this.actionBaton.getParallelBuilder().add(actions);
        if(actionEntity.getProperties() != null && actionEntity.getProperties().getParallel() != null) {
            actionBuilder = actionBuilder.properties(actionEntity.getProperties().getParallel());
        }

        return actionBuilder.build();
    }

    /**
     * Builds a conditional action executor.
     *
     * @param actionEntity The action entity.
     * @param actions The child actions (max 2: if-then and optionally else-then).
     * @return The conditional action.
     * @throws ActionBatonException if configuration is invalid.
     */
     private IAction getConditionalAction(ActionEntity actionEntity, List<IAction> actions) throws ActionBatonException {
        if(actions.isEmpty()) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED,
                    actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        ConditionalExecutorEntity properties = actionEntity.getProperties().getConditional();
        if(properties == null || properties.getPredicateName() == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        if(actionEntity.getActions() == null || actionEntity.getActions().isEmpty() || actionEntity.getActions().size() > 2) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        IConditionalActionBuilder actionBuilder = this.actionBaton.getConditionalBuilder();
        actionBuilder.predicate(getPredicate(properties.getPredicateName())).ifThen(actions.get(0));
        if(actionEntity.getActions().size() == 2) {
            actionBuilder.elseThen(actions.get(1));
        }
        return actionBuilder.build();
    }

    /**
     * Builds a mapper action executor.
     *
     * @param actionEntity The action entity.
     * @return The mapper action.
     * @throws ActionBatonException if configuration is invalid.
     */
     private IAction getMapperAction(ActionEntity actionEntity) throws ActionBatonException {
        MapperExecutorProperties properties = actionEntity.getProperties().getMapper();
        if(properties == null ||  isNullOrEmpty(properties.getMapperName())) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, actionEntity.getName(), actionEntity.getExecutor().toString());
        }
        if(actionEntity.getActions() != null && !actionEntity.getActions().isEmpty()){
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        IMapperActionBatonBuilder actionBuilder = this.actionBaton.getMapperExecutor();
        return actionBuilder.action(this.getAction(actionEntity.getName()))
                .mapper(this.getMapper(properties.getMapperName())).build();
    }

    /**
     * Builds an iterator action executor.
     *
     * @param actionEntity The action entity.
     * @param actions The child action (exactly 1).
     * @return The iterator action.
     * @throws ActionBatonException if configuration is invalid.
     */
    private IAction getIteratorAction(ActionEntity actionEntity, List<IAction> actions) throws ActionBatonException {
        IteratorExecutorProperties properties = actionEntity.getProperties().getIterator();

        if(actions.size() != 1) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED,
                    actionEntity.getName(), actionEntity.getExecutor().toString());
        }
        if(properties == null || isNullOrEmpty(properties.getIteratorName())) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED, actionEntity.getName(), actionEntity.getExecutor().toString());
        }
        IIteratorActionBuilder actionBuilder = this.actionBaton.getIteratorActionBuilder();
        return actionBuilder.add(actions.get(0))
                .iterate(this.getIterator(properties.getIteratorName())).build();
    }

    /**
     * Builds an Action DAG executor.
     *
     * @param actionEntity The action entity.
     * @param actions Child actions (should be empty).
     * @return The DAG action.
     * @throws ActionBatonException if configuration is invalid.
     */
    private IAction getActionDagActionBaton(ActionEntity actionEntity, List<IAction> actions)
            throws ActionBatonException {
        if(!actions.isEmpty()) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.ACTION_ENTITY_VALIDATION_FAILED,
                    actionEntity.getName(), actionEntity.getExecutor().toString());
        }
        if(isNullOrEmpty(actionEntity.getName()) && isNullOrEmpty(actionEntity.getMapperName())) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.INVALID_ACTION_DAG_ENTITY,
                    actionEntity.getName(), actionEntity.getExecutor().toString());
        }

        ActionDagEntity actionDagEntity = null;
        IMapper dynamicActionDagMapper = null;
        if (!isNullOrEmpty(actionEntity.getName())) {
            actionDagEntity = this.getActionDag(actionEntity.getName());
        }
        if (!isNullOrEmpty(actionEntity.getMapperName())) {
            dynamicActionDagMapper = this.getMapper(actionEntity.getMapperName());
        }

        IActionDagExecutorBuilder actionDagBuilder = this.actionBaton.getActionDagActionBuilder()
                .actionDag(actionDagEntity).dynamicActionDag(dynamicActionDagMapper)
                .orchestrator(this);
        return actionDagBuilder.build();
    }

    /**
     * Helper method to check if a string is null or empty.
     *
     * @param value The string to check.
     * @return {@code true} if null or empty, {@code false} otherwise.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
