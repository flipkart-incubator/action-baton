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
package com.flipkart.actionbaton.executor.spi;

import com.flipkart.actionbaton.entities.executor.ParallelExecutorProperties;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;

public class ExecutorServiceProviderJdk21Test {
    @Test
    void createsVirtualThreadsWhenEnabled() {
        ExecutorServiceProviderJdk21 provider = new ExecutorServiceProviderJdk21();
        ParallelExecutorProperties props = new ParallelExecutorProperties();
        props.setUseVirtualThreads(true);

        ExecutorService exec = provider.createExecutor(props);
        assertNotNull(exec);
        exec.shutdown();
        assertTrue(exec.isShutdown());
    }

    @Test
    void createsPlatformThreadsWhenDisabled() {
        ExecutorServiceProviderJdk21 provider = new ExecutorServiceProviderJdk21();
        ParallelExecutorProperties props = new ParallelExecutorProperties();
        props.setUseVirtualThreads(false);
        props.setThreadCount(2);

        ExecutorService exec = provider.createExecutor(props);
        assertNotNull(exec);
        exec.shutdown();
        assertTrue(exec.isShutdown());
    }
}


