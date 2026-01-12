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
package com.baton.tests.orchestrator;

import com.baton.entities.ActionExecutionContext;
import com.baton.entities.actiondag.ActionDagEntity;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.CustomException;
import com.baton.examples.StaticUtils;
import com.baton.examples.orchestrator.ActionOrchestrator;
import com.baton.exception.ActionBatonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public class ActionOrchestratorTests {

    @Test
    public void ActionOrchestrator_Basic_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/BASIC_TEST_1.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_SEQUENTIAL_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/SEQUENTIAL_TEST_1.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_SEQUENTIAL_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/SEQUENTIAL_TEST_2.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }


    @Test
    public void ActionOrchestrator_AsString_SEQUENTIAL_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/SEQUENTIAL_TEST_1.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_AsString_SEQUENTIAL_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/SEQUENTIAL_TEST_2.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("SEQUENTIAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_Parallel_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/PARALLEL_TEST_1.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("PARALLEL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }
    @Test
    public void ActionOrchestrator_Parallel_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/PARALLEL_TEST_2.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("PARALLEL_FLOW_ORCHESTRATOR_2");
        actionOrchestrator.executeActionDag(context, entity);
    }
    @Test
    public void ActionOrchestrator_Parallel_Test3_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/PARALLEL_TEST_3.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("PARALLEL_FLOW_ORCHESTRATOR_3");
        try {
            actionOrchestrator.executeActionDag(context, entity);
        } catch (CustomException e) {
            System.out.println("Exception Value : " + e.value);
            Assertions.assertTrue(true);
        }
        Assertions.assertEquals(1, context.getFailedActions().size());
    }
    @Test
    public void ActionOrchestrator_Conditional_If_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/CONDITIONAL_TEST_1.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("CONDITIONAL_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_Conditional_Else_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/CONDITIONAL_TEST_1.json");
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("CONDITIONAL_FLOW_ORCHESTRATOR_1");
        context.getContext().put("SEQUENCE_COUNTER", 1);
        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_Iterator_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/ITERATOR_TEST_1.json");
        ActionExecutionContext context = new ActionExecutionContextImpl("ITERATOR_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, entity);
    }
    @Test
    public void ActionOrchestrator_Iterator_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/ITERATOR_TEST_1.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("ITERATOR_FLOW_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_InputMapper_Test1_Success_Tests() throws Exception, IOException {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/MAPPER_TEST_1.json");
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("WITHMAPPER_FLOW_ORCHESTRATOR_1");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");

        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_InputMapper_Test2_Success_Tests() throws Exception, IOException {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/MAPPER_TEST_2.json");
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("WITHMAPPER_FLOW_ORCHESTRATOR_1");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");

        actionOrchestrator.executeActionDag(context, entity);
    }

    @Test
    public void ActionOrchestrator_Mixed_Test1_Success_Tests() throws Exception, IOException {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        ActionDagEntity entity = StaticUtils.parseJSONFile("src/test/java/com.baton/tests/actiondags/MIXED_TEST_1.json");
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("MIXED_FLOW_ORCHESTRATOR_1");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");
        context.getContext().put("SEQUENCE_COUNTER", 1);

        actionOrchestrator.executeActionDag(context, entity);
    }


    @Test
    public void ActionOrchestrator_ActionDag_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/ACTION_DAG_TEST_1.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("ACTION_DAG_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_ActionDag_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/ACTION_DAG_TEST_2.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("ACTION_DAG_ORCHESTRATOR_2");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_ActionDag_Test3_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/ACTION_DAG_TEST_3.json")));
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("ACTION_DAG_ORCHESTRATOR_2");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");
        context.getContext().put("SEQUENCE_COUNTER", 1);
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }
    @Test
    public void ActionOrchestrator_DynamicActionDag_Test1_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/DYNAMIC_ACTION_DAG_TEST_1.json")));
        ActionExecutionContext context = new ActionExecutionContextImpl("DYNAMIC_ACTION_DAG_ORCHESTRATOR_1");
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

    @Test
    public void ActionOrchestrator_DynamicActionDag_Test2_Success_Tests() throws Exception {
        ActionOrchestrator actionOrchestrator = new ActionOrchestrator();
        String actionDagJson = new String(Files.readAllBytes(Paths.get("src/test/java/com.baton/tests/actiondags/DYNAMIC_ACTION_DAG_TEST_2.json")));
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("DYNAMIC_ACTION_DAG_ORCHESTRATOR_2");
        context.getContext().put("VALUE_A", "aValue");
        context.getContext().put("VALUE_B", "bValue");
        context.getContext().put("VALUE_C", "cValue");
        context.getContext().put("VALUE_D", "dValue");
        context.getContext().put("SEQUENCE_COUNTER", 1);
        actionOrchestrator.executeActionDag(context, actionDagJson);
    }

}
