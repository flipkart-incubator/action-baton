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
package com.baton.tests.actions;

import com.baton.entities.IAction;
import com.baton.examples.actions.*;
import com.baton.executor.impl.ActionBatonBuilderFactory;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActionBatonTests {
    protected ActionBatonBuilderFactory actionBaton;
    protected List<IAction> actionList1, actionList2;
    protected IAction printAction, printOddAction, printEvenAction, exceptionAction, printIntegerAction, postExecuteExceptionAction, doExecuteExceptionAction;

    @BeforeEach
    public void init(){
        printAction = new PrintAction();
        printOddAction = new PrintOddAction();
        printEvenAction = new PrintEvenAction();
        exceptionAction = new ExecptionAction();
        postExecuteExceptionAction = new ExceptionPostExecute();
        doExecuteExceptionAction = new ExceptionDoExecute();
        printIntegerAction = new PrintIntegerAction();

        actionList1 = getActions1(printAction);
        actionList2 = getActions2(printEvenAction, printOddAction);

        actionBaton = ActionBatonBuilderFactory.getInstance();
    }

    private static List<IAction> getActions2(IAction printOddAction, IAction printEvenAction) {
        List<IAction> actionList2 = new ArrayList<>();
        actionList2.add(printOddAction);
        actionList2.add(printOddAction);
        actionList2.add(printOddAction);
        actionList2.add(printOddAction);
        actionList2.add(printOddAction);
        actionList2.add(printEvenAction);
        actionList2.add(printEvenAction);
        actionList2.add(printEvenAction);
        actionList2.add(printEvenAction);
        actionList2.add(printEvenAction);
        return actionList2;
    }

    private static List<IAction> getActions1(IAction printAction) {
        List<IAction> taskList = new ArrayList<>();
        taskList.add(printAction);
        taskList.add(printAction);
        taskList.add(printAction);
        taskList.add(printAction);
        return taskList;
    }
}
