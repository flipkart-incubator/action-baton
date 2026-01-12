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
package com.baton.executor.spi;

import com.baton.entities.executor.ParallelExecutorProperties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDK 21 specific implementation of {@link IExecutorServiceProvider} that supports Virtual Threads.
 */
public final class ExecutorServiceProviderJdk21 implements IExecutorServiceProvider {
    /**
     * {@inheritDoc}
     * If {@code useVirtualThreads} is set to {@code true}, it returns a Virtual Thread per task executor.
     * Otherwise, it falls back to a fixed platform thread pool.
     */
    @Override
    public ExecutorService createExecutor(ParallelExecutorProperties properties) {
        if (properties.isUseVirtualThreads()) {
            return Executors.newVirtualThreadPerTaskExecutor();
        }
        return Executors.newFixedThreadPool(properties.getThreadCount());
    }
}


