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
import com.baton.entities.executor.ParallelExecutorProperties;
import com.baton.examples.ActionExecutionContextImpl;
import com.baton.examples.actions.HttpCallAction;
import com.baton.examples.actions.NoOpAction;
import com.baton.executor.impl.ActionBatonBuilderFactory;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LatencyBenchmarkTest {

    @SafeVarargs
    private static List<Long> runSequential(IAction<ActionExecutionContextImpl, Exception>... actions) throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("BENCH_SEQ");
        List<Long> samples = new ArrayList<>();
        for (IAction<ActionExecutionContextImpl, Exception> action : actions) {
            Instant start = Instant.now();
            ActionBatonBuilderFactory.getInstance()
                    .getSequenceBuilder()
                    .add((IAction) action)
                    .build()
                    .execute(context);
            samples.add(Duration.between(start, Instant.now()).toNanos());
        }
        return samples;
    }

    private static List<Long> runParallel(List<IAction<ActionExecutionContextImpl, Exception>> actions, int threadCount) throws Exception {
        ActionExecutionContextImpl context = new ActionExecutionContextImpl("BENCH_PAR");
        Instant start = Instant.now();
        ParallelExecutorProperties props = new ParallelExecutorProperties();
        props.setThreadCount(threadCount);
        props.setTimeout(10_000);
        ActionBatonBuilderFactory.getInstance()
                .getParallelBuilder()
                .add(new ArrayList<IAction>(actions))
                .properties(props)
                .build()
                .execute(context);
        return Collections.singletonList(Duration.between(start, Instant.now()).toNanos());
    }

    private static long percentileNs(List<Long> samples, double p) {
        if (samples.isEmpty()) return 0;
        samples.sort(Comparator.naturalOrder());
        int idx = (int) Math.ceil(p * samples.size()) - 1;
        idx = Math.max(0, Math.min(idx, samples.size() - 1));
        return samples.get(idx);
    }

    private static String fmtMicros(long nanos) {
        return String.format("%.3f ms", nanos / 1_000_000.0);
    }

    @Test
    public void benchmark_noop_and_http() throws Exception {
        int iterations = 200;

        // No-op sequential
        List<Long> noop = new ArrayList<>();
        NoOpAction noOpAction = new NoOpAction();
        for (int i = 0; i < iterations; i++) {
            noop.addAll(runSequential(noOpAction));
        }

        // HTTP GET to localhost (ensure something responds, e.g., 127.0.0.1:8080)
        String url = System.getProperty("benchmark.url", "http://127.0.0.1:8080/");
        HttpCallAction httpAction = new HttpCallAction(url, 2000);
        List<Long> http = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            try {
                http.addAll(runSequential(httpAction));
            } catch (Exception ex) {
                System.out.println("HTTP benchmark skipped (no local server?): " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
                break;
            }
        }

        long noopP50 = percentileNs(noop, 0.50);
        long noopP95 = percentileNs(noop, 0.95);
        long httpP50 = percentileNs(http, 0.50);
        long httpP95 = percentileNs(http, 0.95);

        System.out.println("Action Executor Latency (Sequential)");
        System.out.println("NoOp:  p50=" + fmtMicros(noopP50) + ", p95=" + fmtMicros(noopP95));
        System.out.println("HTTP:   p50=" + fmtMicros(httpP50) + ", p95=" + fmtMicros(httpP95) + " (URL=" + url + ")");

        // Parallel fan-out sanity (10 no-ops)
        List<IAction<ActionExecutionContextImpl, Exception>> manyNoops = new ArrayList<>();
        for (int i = 0; i < 10; i++) manyNoops.add(new NoOpAction());
        List<Long> parallelBatch = runParallel(manyNoops, 4);
        System.out.println("Parallel 10x NoOp batch time: " + fmtMicros(parallelBatch.get(0)));
    }
}



