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

import com.baton.entities.ActionExecutionContext;
import com.baton.entities.ActionExecutionType;
import com.baton.entities.IAction;
import com.baton.exception.ActionBatonException;
import com.baton.executor.IActionBaton;
import com.baton.executor.builder.IDefaultActionBuilder;

/**
 * Default executor that runs a single action.
 */
final class DefaultActionBaton extends AbstractActionBaton {
    /**
     * The action to execute.
     */
    private final IAction action;

    /**
     * Constructs a new default action baton.
     *
     * @param action The action to execute.
     */
    DefaultActionBaton(IAction action) {
        super(ActionExecutionType.DEFAULT);
        this.action = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        executeAction(context, this.action);
    }
}

/**
 * Builder for creating {@link DefaultActionBaton} instances.
 */
final class DefaultActionBuilder implements IDefaultActionBuilder {

    /**
     * Internal action being built.
     */
    private IAction action;

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(this.action == null) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "Single action is required for Default Action");
        return new DefaultActionBaton(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDefaultActionBuilder add(IAction action) throws ActionBatonException {
        this.action = action;
        return this;
    }
}
