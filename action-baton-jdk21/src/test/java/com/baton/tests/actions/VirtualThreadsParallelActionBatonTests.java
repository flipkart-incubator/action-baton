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

import com.baton.entities.ActionExecutionContext;
import com.baton.entities.IAction;
import com.baton.entities.executor.ParallelExecutorProperties;
import com.baton.executor.builder.IParallelActionBuilder;
import com.baton.executor.impl.ActionBatonBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VirtualThreadsParallelActionBatonTests {

    static class TestContext extends ActionExecutionContext<String, Map<String, Object>> {
        public TestContext(String workflowId) {
            super(workflowId, new ConcurrentHashMap<>());
        }
        @Override
        public Map<String, Object> getContext() {
            return super.context;
        }
    }

    static class IncrementAction implements IAction<ActionExecutionContext, Exception> {
        private final String name;
        private final AtomicInteger counter;
        private final long sleepMillis;
        private final List<Boolean> virtualFlags;

        IncrementAction(String name, AtomicInteger counter, long sleepMillis, List<Boolean> virtualFlags) {
            this.name = name;
            this.counter = counter;
            this.sleepMillis = sleepMillis;
            this.virtualFlags = virtualFlags;
        }

        @Override
        public String getActionName() { return name; }

        @Override
        public void execute(ActionExecutionContext context) throws Exception {
            if (sleepMillis > 0) {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            }
            counter.incrementAndGet();
            if (virtualFlags != null) {
                virtualFlags.add(Thread.currentThread().isVirtual());
            }
        }
    }

    @Test
    public void parallel_executor_uses_virtual_threads_when_enabled() throws Exception {
        TestContext context = new TestContext("VIRTUAL_PARALLEL");
        IParallelActionBuilder builder = ActionBatonBuilderFactory.getInstance().getParallelBuilder();

        // Many short tasks to highlight virtual thread behavior (should not block on small threadCount)
        int taskCount = 200;
        AtomicInteger counter = new AtomicInteger(0);
        List<Boolean> virtualFlags = Collections.synchronizedList(new ArrayList<>());
        List<IAction> actions = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            actions.add(new IncrementAction("INC_" + i, counter, 5, virtualFlags));
        }

        ParallelExecutorProperties props = new ParallelExecutorProperties();
        props.setThreadCount(2); // would be restrictive for platform threads
        props.setTimeout(10_000);
        props.setUseVirtualThreads(true);

        builder.add(actions).properties(props).build().execute(context);

        Assertions.assertEquals(taskCount, counter.get());
        Assertions.assertEquals(0, context.getFailedActions().size());
        Assertions.assertEquals(taskCount, context.getResults().size());
        // All tasks should have executed on virtual threads
        Assertions.assertEquals(taskCount, virtualFlags.size());
        Assertions.assertTrue(virtualFlags.stream().allMatch(Boolean::booleanValue));
    }

    @Test
    public void parallel_executor_falls_back_to_platform_when_disabled() throws Exception {
        TestContext context = new TestContext("PLATFORM_PARALLEL");
        IParallelActionBuilder builder = ActionBatonBuilderFactory.getInstance().getParallelBuilder();

        int taskCount = 20;
        AtomicInteger counter = new AtomicInteger(0);
        List<Boolean> virtualFlags = Collections.synchronizedList(new ArrayList<>());
        List<IAction> actions = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            actions.add(new IncrementAction("INC_" + i, counter, 1, virtualFlags));
        }

        ParallelExecutorProperties props = new ParallelExecutorProperties();
        props.setThreadCount(4);
        props.setTimeout(10_000);
        props.setUseVirtualThreads(false);

        builder.add(actions).properties(props).build().execute(context);

        Assertions.assertEquals(taskCount, counter.get());
        Assertions.assertEquals(0, context.getFailedActions().size());
        Assertions.assertEquals(taskCount, context.getResults().size());
        // All tasks should have executed on platform threads
        Assertions.assertEquals(taskCount, virtualFlags.size());
        Assertions.assertTrue(virtualFlags.stream().noneMatch(Boolean::booleanValue));
    }
}


