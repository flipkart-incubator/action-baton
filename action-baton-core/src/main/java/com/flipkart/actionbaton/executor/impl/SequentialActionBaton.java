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

import com.flipkart.actionbaton.entities.IAction;
import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.entities.ActionExecutionContext;
import com.flipkart.actionbaton.entities.ActionExecutionType;
import com.flipkart.actionbaton.executor.IActionBaton;
import com.flipkart.actionbaton.executor.builder.ISequenceActionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Executor that runs a list of actions sequentially in the order they were added.
 */
final class SequentialActionBaton extends AbstractActionBaton {

    /**
     * The list of actions to be executed sequentially.
     */
    private final List<IAction> actionList;

    /**
     * Constructs a new sequential action baton.
     *
     * @param actionList The list of actions.
     */
    SequentialActionBaton(List<IAction> actionList) {
        super(ActionExecutionType.SEQUENTIAL);
        this.actionList = actionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        for(IAction action : this.actionList){
            executeAction(context, action);
        }
    }
}

/**
 * Builder for creating {@link SequentialActionBaton} instances.
 */
final class SequentialActionBuilder implements ISequenceActionBuilder {
    /**
     * Internal list of actions being built.
     */
    private final List<IAction> actionList = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    public SequentialActionBuilder add(List<IAction> actionList){
        this.actionList.addAll(actionList);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public SequentialActionBuilder add(IAction action){
        this.actionList.add(action);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public IActionBaton build() throws ActionBatonException {
        if(actionList.isEmpty()) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "At least one Action is required for Sequential Action");

        return new SequentialActionBaton(actionList);
    }
}
