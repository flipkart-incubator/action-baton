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
package com.baton.examples.orchestrator;

import com.baton.entities.IAction;
import com.baton.entities.IIterator;
import com.baton.entities.IMapper;
import com.baton.entities.IPredicate;
import com.baton.entities.actiondag.ActionDagEntity;
import com.baton.examples.StaticUtils;
import com.baton.examples.actions.*;
import com.baton.examples.iterator.RandomIntIterator;
import com.baton.examples.mapper.ActionDagMapper;
import com.baton.examples.mapper.MixedActionDagMapper;
import com.baton.examples.mapper.ParallelSampleMapper;
import com.baton.examples.mapper.SampleMapper;
import com.baton.examples.predicate.EvenPredicate;
import com.baton.orchestrator.impl.BaseActionOrchestrator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ActionOrchestrator extends BaseActionOrchestrator {

    @Override
    public IAction getAction(String actionName) {
        switch (actionName) {
            case "PRINT_TASK" : return new PrintAction();
            case "PRINT_ACTION_WITH_MAPPER_TASK" : return new PrintActionWithMapper();
            case "PRINT_EVEN_TASK" : return new PrintEvenAction();
            case "PRINT_ODD_TASK" : return new PrintOddAction();
            case "PRINT_INTEGER_ACTION" : return new PrintIntegerAction();
            case "EXCEPTION_ACTION" : return new ExecptionAction();
        }
        return null;
    }

    @Override
    public IMapper getMapper(String mapperName) {
        switch (mapperName) {
            case "ParallelSampleMapper" : return new ParallelSampleMapper();
            case "ACTION_DAG_MAPPER" : return new ActionDagMapper();
            case "MIXED_ACTION_DAG_MAPPER" : return new MixedActionDagMapper();
            case "SampleMapper" : return new SampleMapper();
        }
        return null;

    }

    @Override
    public ActionDagEntity getActionDag(String actionDagName) {
        try {
            return StaticUtils.parseJSONFile("src/test/java/com/baton/tests/actiondags/" + actionDagName + ".json");
        } catch (IOException e) {

        }
        return super.getActionDag(actionDagName);
    }

    @Override
    public IPredicate getPredicate(String predicateName) {
        return new EvenPredicate();
    }

    @Override
    public IIterator getIterator(String iteratorName) {
        return new RandomIntIterator();
    }
}
