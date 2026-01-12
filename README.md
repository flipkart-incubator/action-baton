<!--
Copyright 2026 ActionBaton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

# ActionBaton

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

ActionBaton is a lightweight, developer-friendly Java library for internal workflow orchestration. It supports sequential, parallel, conditional, and mixed execution patterns with minimal dependencies.

---

## 📑 Index
- [Why ActionBaton?](#-why-actionbaton)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Execution Patterns](#-execution-patterns)
- [Configuration-Driven Workflows](#-configuration-driven-workflows)
- [Detailed Code Examples](docs/EXAMPLES.md)
- [Comparison and Real-World Use Cases](docs/COMPARISON_AND_USE_CASES.md)
- [Architecture](#-architecture)
- [Contributors](#-contributors)
- [License](#-license)

---

## 🚀 Why ActionBaton?
- **Lightweight**: ~500KB with minimal dependencies (Jackson, SLF4J).
- **Flexible**: Mix sequential, parallel, conditional, iterator, and mapper patterns.
- **Java 21 Ready**: Support for Virtual Threads in parallel execution.
- **Self-contained**: No external infrastructure or databases required.

---

## 📦 Installation (In Progress)

> **Note**: The libraries are currently not published to Maven Central. This section will be updated once they are available.

### Maven
```xml
<!-- For Java 17+ -->
<dependency>
    <groupId>com.baton</groupId>
    <artifactId>action-baton-jdk17</artifactId>
    <version>1.1.3</version>
</dependency>

<!-- For Java 21+ (Virtual Thread Support) -->
<dependency>
    <groupId>com.baton</groupId>
    <artifactId>action-baton-jdk21</artifactId>
    <version>1.1.3</version>
</dependency>
```

---

## 📖 Quick Start

### 1. Define an Action
```java
public class SendEmailAction implements IAction<ActionExecutionContext, Exception> {
    @Override
    public String getActionName() { return "SEND_EMAIL"; }
    
    @Override
    public void execute(ActionExecutionContext context) throws Exception {
        String email = (String) context.getContext().get("email");
        // Business logic here
    }
}
```

### 2. Orchestrate and Execute
```java
ActionExecutionContext context = new ActionExecutionContextImpl("ONBOARDING");
context.getContext().put("email", "user@example.com");

ActionBatonBuilderFactory.getInstance()
    .getSequenceBuilder()
    .add(new ValidateUserAction())
    .add(new SendEmailAction())
    .build()
    .execute(context);
```

---

## 🎯 Execution Patterns

### Sequential
```java
factory.getSequenceBuilder()
    .add(new Step1Action())
    .add(new Step2Action())
    .build()
    .execute(context);
```

### Parallel
```java
ParallelExecutorProperties props = new ParallelExecutorProperties();
props.setThreadCount(5);
props.setUseVirtualThreads(true); // Java 21+

factory.getParallelBuilder()
    .add(Arrays.asList(new TaskA(), new TaskB()))
    .properties(props)
    .build()
    .execute(context);
```

### Conditional
```java
factory.getConditionalBuilder()
    .predicate(new MyPredicate())
    .ifThen(new SuccessAction())
    .elseThen(new FailureAction())
    .build()
    .execute(context);
```

### Iterator
Iterates over a collection provided by an `IIterator` and executes an action for each item.
```java
factory.getIteratorActionBuilder()
    .iterate(new MyIterator()) // Returns List<V>
    .add(new ProcessItemAction())
    .build()
    .execute(context);
```

### Mapper
Decouples action logic by transforming the `ActionExecutionContext` into a specific input object.
```java
factory.getMapperExecutor()
    .mapper(new MyMapper())
    .action(new UnderlyingAction())
    .build()
    .execute(context);
```

### ActionDag
Executes a complex Directed Acyclic Graph (DAG) defined programmatically or via configuration.
```java
factory.getActionDagActionBuilder()
    .actionDag(myDagEntity)
    .orchestrator(myOrchestrator)
    .build()
    .execute(context);
```

### Mixed (Complex Flows)
Mix and match different patterns to create complex workflows.
```java
factory.getSequenceBuilder()
    .add(new InitializeAction())
    .add(factory.getParallelBuilder()
        .add(new ParallelTask1())
        .add(new ParallelTask2())
        .build())
    .add(factory.getConditionalBuilder()
        .predicate(new ShouldContinuePredicate())
        .ifThen(new FinalStepAction())
        .build())
    .build()
    .execute(context);
```

---

## 🔧 Configuration-Driven Workflows
ActionBaton supports declarative workflows via JSON:
```json
{
  "actionDagId": "USER_FLOW",
  "rootAction": {
    "executor": "SEQUENTIAL",
    "actions": [
      { "name": "VALIDATE" },
      {
        "executor": "PARALLEL",
        "actions": [{ "name": "EMAIL" }, { "name": "LOG" }]
      }
    ]
  }
}
```
Execute using `ActionOrchestrator`:
```java
orchestrator.executeActionDag(context, dagJson);
```

---

## 📊 Comparison and Use Cases
For a detailed comparison with solutions like **Apache Camel**, **Dexecutor**, and **CompletableFuture**, as well as more real-world examples, see:
👉 **[Comparison and Real-World Use Cases](docs/COMPARISON_AND_USE_CASES.md)**

---

## 🏗️ Architecture
ActionBaton uses a layered approach:
1. **Core Interfaces**: `IAction`, `IPredicate`, `IMapper`, `IIterator` (See [Code Examples](docs/EXAMPLES.md)).
2. **Builders**: Fluent API for programmatic definition.
3. **Executors**: Implementations for different patterns (Sequential, Parallel, etc.).
4. **Context**: `ActionExecutionContext` for state management across steps.

---

## 👥 Contributors
- **Mridul Mundara** - [mridul.mundara@flipkart.com](mailto:mridul.mundara@flipkart.com)

---

## 📄 License
This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---
<p align="center">Making workflow orchestration simple and lightweight.</p>
