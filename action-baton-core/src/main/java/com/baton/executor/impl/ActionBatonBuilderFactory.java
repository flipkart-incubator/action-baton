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
import com.baton.entities.IAction;
import com.baton.exception.ActionBatonException;
import com.baton.executor.builder.*;


/**
 * Singleton factory for creating various types of action builders.
 * Use this class to programmatically define and build workflow executors.
 */
public class ActionBatonBuilderFactory {
    /**
     * The singleton instance of the factory.
     */
    private static volatile ActionBatonBuilderFactory actionBaton;

    private ActionBatonBuilderFactory(){}

    /**
     * Returns the singleton instance of {@link ActionBatonBuilderFactory}.
     *
     * @return The factory instance.
     */
    public static ActionBatonBuilderFactory getInstance(){
        if(actionBaton == null){
            synchronized (ActionBatonBuilderFactory.class) {
                if(actionBaton == null) {
                    actionBaton = new ActionBatonBuilderFactory();
                }
            }
        }
        return actionBaton;
    }

    /**
     * Returns a builder for creating sequential action executors.
     *
     * @return A {@link ISequenceActionBuilder} instance.
     */
    public ISequenceActionBuilder getSequenceBuilder() { return new SequentialActionBuilder(); }

    /**
     * Returns a builder for creating parallel action executors.
     *
     * @return A {@link IParallelActionBuilder} instance.
     */
    public IParallelActionBuilder getParallelBuilder() { return new ParallelActionBuilder(); }

    /**
     * Returns a builder for creating conditional action executors.
     *
     * @return A {@link IConditionalActionBuilder} instance.
     */
    public IConditionalActionBuilder getConditionalBuilder() { return new ConditionalActionBuilder(); }

    /**
     * Returns a builder for creating mapper-based action executors.
     *
     * @return A {@link IMapperActionBatonBuilder} instance.
     */
    public IMapperActionBatonBuilder getMapperExecutor(){ return new MapperActionBatonBuilder(); }

    /**
     * Returns a default builder for simple actions.
     *
     * @return A {@link IDefaultActionBuilder} instance.
     */
    public IDefaultActionBuilder getDefaultActionBuilder() { return new DefaultActionBuilder(); }

    /**
     * Returns a builder for creating iterator-based action executors.
     *
     * @return A {@link IIteratorActionBuilder} instance.
     */
    public IIteratorActionBuilder getIteratorActionBuilder() { return new IteratorActionBuilder(); }

    /**
     * Returns a builder for creating Action DAG executors from programmatically defined DAGs.
     *
     * @return A {@link IActionDagExecutorBuilder} instance.
     */
    public IActionDagExecutorBuilder getActionDagActionBuilder() { return new ActionDagExecuteBuilder();}
}
