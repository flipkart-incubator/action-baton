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
package com.flipkart.actionbaton.entities.actiondag;

import com.flipkart.actionbaton.entities.ActionExecutionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a single action node in an Action DAG configuration.
 * It can be a leaf action or a composite action (executor) containing child actions.
 */
@Getter
@Setter
@NoArgsConstructor
public class ActionEntity {
    /**
     * The unique name of the action or executor.
     */
    private String name;

    /**
     * Configuration properties for the executor (e.g., parallel thread count).
     */
    private ActionProperties properties;

    /**
     * The type of executor to use for this node. Defaults to {@link ActionExecutionType#DEFAULT}.
     */
    private ActionExecutionType executor = ActionExecutionType.DEFAULT;

    /**
     * List of child actions if this entity is a composite executor (e.g., SEQUENTIAL, PARALLEL).
     */
    private List<ActionEntity> actions;

    /**
     * Optional mapper name for default actions that require data transformation.
     */
    private String mapperName;
}
