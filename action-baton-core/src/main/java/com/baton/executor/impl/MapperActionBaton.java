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

import com.baton.commons.utils.ActionBatonConstants;
import com.baton.entities.ActionExecutionContext;
import com.baton.entities.ActionExecutionType;
import com.baton.entities.IAction;
import com.baton.entities.IMapper;
import com.baton.exception.ActionBatonException;
import com.baton.executor.IActionBaton;
import com.baton.executor.builder.IMapperActionBatonBuilder;

/**
 * Executor that uses a {@link IMapper} to transform input and output data for an action.
 */
final class MapperActionBaton extends AbstractActionBaton {

    /**
     * The mapper to use for data transformation.
     */
    private final IMapper mapper;

    /**
     * The action to execute.
     */
    private final IAction action;

    /**
     * Constructs a new mapper action baton.
     *
     * @param mapper The data mapper.
     * @param action The action to execute.
     */
    MapperActionBaton(IMapper mapper, IAction action) {
        super(ActionExecutionType.MAPPER);
        this.action = action;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        Object object = mapper.mapInput(context);
        String inputMapperKey = ActionBatonConstants.getInputMapperKey(this.action.getActionName());
        String outputMapperKey = ActionBatonConstants.getOutputMapperKey(this.action.getActionName());

        context.getLocalContext().put(inputMapperKey, object);
        /*
          Client needs to write code for executing the action and put output in context.localContext with key from
          `ActionBatonConstants.getOutputMapperKey(this.action.getActionName())`
        */
        executeAction(context, action);
        mapper.mapOutput(context);

        context.getLocalContext().remove(inputMapperKey);
        context.getLocalContext().remove(outputMapperKey);
    }
}

/**
 * Builder for creating {@link MapperActionBaton} instances.
 */
final class MapperActionBatonBuilder implements IMapperActionBatonBuilder {
    /**
     * Internal action being built.
     */
    private IAction action;

    /**
     * Internal mapper being built.
     */
    private IMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(this.action == null) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "Action is required for InputMapper Action");

        if(mapper == null) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "Mapper is required for InputMapper Action");

        return new MapperActionBaton(mapper, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMapperActionBatonBuilder mapper(IMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMapperActionBatonBuilder action(IAction action) {
        this.action = action;
        return this;
    }
}
