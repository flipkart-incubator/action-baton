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

import com.flipkart.actionbaton.entities.*;
import com.flipkart.actionbaton.exception.ActionBatonException;
import com.flipkart.actionbaton.executor.IActionBaton;
import com.flipkart.actionbaton.executor.builder.IConditionalActionBuilder;

/**
 * Executor that runs one of two actions based on the evaluation of a {@link IPredicate}.
 */
final class ConditionalActionBaton extends AbstractActionBaton {
    /**
     * The predicate to evaluate.
     */
    private final IPredicate predicate;

    /**
     * The action to execute if the predicate evaluates to {@code true}.
     */
    private final IAction ifThen;

    /**
     * The action to execute if the predicate evaluates to {@code false}.
     */
    private final IAction elseThen;

    /**
     * Constructs a new conditional action baton.
     *
     * @param predicate The condition to check.
     * @param ifThen The action for the 'true' branch.
     * @param elseThen The action for the 'false' branch (can be null).
     */
    ConditionalActionBaton(IPredicate predicate, IAction ifThen, IAction elseThen) {
        super(ActionExecutionType.CONDITIONAL);
        this.predicate = predicate;
        this.ifThen = ifThen;
        this.elseThen = elseThen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        if(this.predicate.verify(context)) {
            executeAction(context, ifThen);
        } else if(this.elseThen != null) {
            executeAction(context, elseThen);
        }
    }
}

/**
 * Builder for creating {@link ConditionalActionBaton} instances.
 */
final class ConditionalActionBuilder implements IConditionalActionBuilder {

    /**
     * Internal predicate being built.
     */
    private IPredicate predicate;

    /**
     * Internal 'if' action being built.
     */
    private IAction ifThen;

    /**
     * Internal 'else' action being built.
     */
    private IAction elseThen;

    /**
     * {@inheritDoc}
     */
    @Override
    public IActionBaton build() throws ActionBatonException {
        if(this.predicate == null) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "Predicate is required for Conditional Action");
        if(this.ifThen == null) throw new ActionBatonException(ActionBatonException.ErrorCode.WRONG_ACTION_BUILDER,
                "If Action is required for Conditional Action");

        return new ConditionalActionBaton(this.predicate, this.ifThen, this.elseThen);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConditionalActionBuilder predicate(IPredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConditionalActionBuilder ifThen(IAction action) throws ActionBatonException {
        this.ifThen = action;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConditionalActionBuilder elseThen(IAction action) throws ActionBatonException {
        this.elseThen = action;
        return this;
    }
}
