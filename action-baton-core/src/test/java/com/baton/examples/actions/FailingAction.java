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
package com.baton.examples.actions;

import com.baton.entities.ActionExecutionContext;
import com.baton.entities.IAction;
import com.baton.examples.ActionExecutionContextImpl;

import java.io.IOException;

/**
 * Action that fails a specified number of times before succeeding.
 * Useful for testing failure scenarios in workflows.
 */
public class FailingAction implements IAction<ActionExecutionContextImpl, Exception> {
    private final String name;
    private int attemptCount = 0;
    private final int failUntilAttempt;
    private final Class<? extends Exception> exceptionType;

    public FailingAction(String name, int failUntilAttempt) {
        this(name, failUntilAttempt, IOException.class);
    }

    public FailingAction(String name, int failUntilAttempt, Class<? extends Exception> exceptionType) {
        this.name = name;
        this.failUntilAttempt = failUntilAttempt;
        this.exceptionType = exceptionType;
    }

    @Override
    public String getActionName() {
        return name;
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws Exception {
        attemptCount++;
        if (attemptCount < failUntilAttempt) {
            Exception exception;
            try {
                exception = exceptionType.getDeclaredConstructor(String.class)
                        .newInstance("Simulated failure on attempt " + attemptCount);
            } catch (Exception e) {
                exception = new IOException("Simulated failure on attempt " + attemptCount);
            }
            throw exception;
        }
        // Success
        context.getContext().put("SUCCESS_ATTEMPT", attemptCount);
    }

    public int getAttemptCount() {
        return attemptCount;
    }
}

