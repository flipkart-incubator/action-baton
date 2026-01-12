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
package com.baton.orchestrator;

import com.baton.entities.*;
import com.baton.entities.actiondag.ActionDagEntity;

/**
 * High-level orchestration interface for managing and executing workflows.
 * Orchestrators are responsible for resolving action, mapper, predicate, and iterator names
 * into their respective implementations and executing Action DAGs.
 */
public interface IActionOrchestrator {

    /**
     * Resolves an action by its unique name.
     *
     * @param actionName The name of the action.
     * @return The resolved {@link IAction} instance.
     */
    IAction getAction(String actionName);

    /**
     * Resolves a mapper by its unique name.
     *
     * @param mapperName The name of the mapper.
     * @return The resolved {@link IMapper} instance.
     */
    IMapper getMapper(String mapperName);

    /**
     * Resolves a predicate by its unique name.
     *
     * @param predicateName The name of the predicate.
     * @return The resolved {@link IPredicate} instance.
     */
    IPredicate getPredicate(String predicateName);

    /**
     * Resolves an iterator by its unique name.
     *
     * @param iteratorName The name of the iterator.
     * @return The resolved {@link IIterator} instance.
     */
    IIterator getIterator(String iteratorName);

    /**
     * Optional method to resolve an Action DAG by its name.
     *
     * @param actionDagName The name of the Action DAG.
     * @return The resolved {@link ActionDagEntity} instance, or {@code null} if not found.
     */
    default ActionDagEntity getActionDag(String actionDagName) {
        return null;
    }

    /**
     * Executes a workflow defined by an {@link ActionDagEntity}.
     *
     * @param context The execution context.
     * @param actionDagEntity The DAG entity defining the workflow.
     * @throws Exception if an error occurs during execution.
     */
    void executeActionDag(ActionExecutionContext context, ActionDagEntity actionDagEntity) throws Exception;

    /**
     * Executes a workflow defined by a JSON string representation of an Action DAG.
     *
     * @param context The execution context.
     * @param actionDag The JSON string defining the workflow.
     * @throws Exception if an error occurs during execution or parsing.
     */
    void executeActionDag(ActionExecutionContext context, String actionDag) throws Exception;

}
