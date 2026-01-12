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

import com.baton.entities.executor.ParallelExecutorProperties;
import com.baton.executor.spi.IExecutorServiceProvider;
import com.baton.executor.spi.DefaultExecutorServiceProvider;

import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Internal utility class for managing and selecting {@link IExecutorServiceProvider} implementations.
 * Uses {@link ServiceLoader} to discover available providers, preferring specialized providers (e.g., JDK 21 Virtual Threads) over the default one.
 */
final class ExecutorServices {
    /**
     * The selected executor service provider.
     */
    private static volatile IExecutorServiceProvider selected;

    private ExecutorServices() {}

    /**
     * Returns the selected provider, discovering it if necessary.
     *
     * @return The {@link IExecutorServiceProvider} instance.
     */
    private static IExecutorServiceProvider getSelected() {
        if (selected != null) return selected;
        synchronized (ExecutorServices.class) {
            if (selected != null) return selected;
            IExecutorServiceProvider fallback = null;
            for (IExecutorServiceProvider p : ServiceLoader.load(IExecutorServiceProvider.class)) {
                if (p instanceof DefaultExecutorServiceProvider) {
                    fallback = p; // keep as fallback
                    continue;
                }
                selected = p; // prefer a non-default provider
                break;
            }
            if (selected == null) selected = (fallback != null) ? fallback : new DefaultExecutorServiceProvider();
            return selected;
        }
    }

    /**
     * Creates an {@link ExecutorService} based on the provided configuration.
     *
     * @param properties Parallel execution properties.
     * @return A new {@link ExecutorService} instance.
     */
    static ExecutorService createExecutor(ParallelExecutorProperties properties) {
        IExecutorServiceProvider provider = getSelected();
        if (provider != null) {
            return provider.createExecutor(properties);
        }
        // Fallback when no provider module present: use platform thread pool
        return Executors.newFixedThreadPool(properties.getThreadCount());
    }
}


