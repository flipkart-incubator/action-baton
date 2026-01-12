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
import com.baton.entities.IIterator;
import com.baton.exception.ActionBatonException;
import com.baton.executor.IActionBaton;
import com.baton.executor.builder.IIteratorActionBuilder;

import java.util.List;

/**
 * Executor that iterates over a collection of items and runs a specific action for each item.
 */
final class IteratorActionBaton extends AbstractActionBaton {
    /**
     * The iterator that provides the list of items.
     */
    private final IIterator iterator;

    /**
     * The action to execute for each item.
     */
    private final IAction action;

    /**
     * Constructs a new iterator action baton.
     *
     * @param iterator The item provider.
     * @param action The action to run per item.
     */
    IteratorActionBaton(IIterator iterator, IAction action ) {
        super(ActionExecutionType.ITERATOR);
        this.iterator = iterator;
        this.action = action;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        List iterators = this.iterator.getIterator(context);
        String iteratorKey = ActionBatonConstants.getIteratorKey();

        for (Object it : iterators) {
            context.getLocalContext().put(iteratorKey, it);
            executeAction(context, action);
            context.getLocalContext().remove(iteratorKey);
        }
    }
}

/**
 * Builder for creating {@link IteratorActionBaton} instances.
 */
final class IteratorActionBuilder implements IIteratorActionBuilder {

    /**
     * Internal iterator being built.
     */
    private IIterator iterator;

    /**
     * Internal action being built.
     */
    private IAction action;

    /**
     * {@inheritDoc}
     */
    @Override
    public IIteratorActionBuilder iterate(IIterator iterator) {
        this.iterator = iterator;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IIteratorActionBuilder add(List<IAction> actionList) throws ActionBatonException {
        throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "Empty Action List is not allowed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IIteratorActionBuilder add(IAction action) throws ActionBatonException {
        if(action == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Action cannot be null");
        }
        this.action = action;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(this.iterator == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Iterator cannot be null");
        }
        if(this.action == null) {
            throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                    "Action cannot be null");
        }
        return new IteratorActionBaton(this.iterator, this.action);
    }
}
