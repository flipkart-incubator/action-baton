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

# Comparison and Real-World Use Cases

This document provides a detailed comparison of ActionBaton with other common workflow orchestration and integration solutions, along with real-world examples of how to use ActionBaton effectively.

---

## 📊 Comparison with Similar Solutions

| Approach | ActionBaton | Dexecutor | Custom CompletableFuture | Hand-rolled Solutions |
|----------|----------------|-----------|------------------------|---------------------|
| **Learning Curve** | ⭐⭐ Easy | ⭐⭐⭐ Medium | ⭐⭐⭐⭐ Complex | ⭐⭐⭐⭐⭐ Very Complex |
| **Setup Time** | Minutes | Minutes | Hours | Days/Weeks |
| **Conditional Logic** | ✅ Built-in | ❌ Manual | ❌ Manual | ✅ Custom |
| **JSON Configuration** | ✅ Native | ❌ | ❌ | ❌ Custom |
| **Error Handling** | ✅ Built-in | Basic | Manual | Custom |
| **Testing Support** | ✅ Easy | Medium | Complex | Custom |
| **Maintenance** | ✅ Library Updates | ✅ Library Updates | Manual | Full Ownership |

### Alternative Approaches & When to Use Them:

**🔧 Use `CompletableFuture` chains when:**
- Simple async operations (2-3 steps)
- You need reactive streams integration
- Maximum performance is critical

**🔧 Use Dexecutor when:**
- Task dependency graphs are your primary concern
- You don't need conditional logic
- Minimal dependencies are crucial

**🔧 Use Apache Camel when:**
- You need built-in connectors (300+ components for various systems)
- Enterprise Integration Patterns are required (Content-Based Router, Aggregator, Splitter, etc.)
- You need advanced routing, transformation, and mediation capabilities
- You're building a comprehensive integration platform

**🔧 Use Spring Batch when:**
- Processing large volumes of data (millions of records)
- You need chunk-oriented processing
- Database transaction management is critical

**🔧 Build custom solution when:**
- Your workflow requirements are highly specialized
- You need extreme performance optimization
- You have team expertise and time to maintain it

---

## 🔄 ActionBaton vs Apache Camel

Since Apache Camel is the most well-known integration framework, here's a focused comparison:

### When ActionBaton is Better:

| Aspect | ActionBaton | Apache Camel |
|--------|----------------|-------------|
| **Learning Curve** | Simple Java interfaces | EIP patterns + DSL syntax |
| **Dependencies** | ~500KB | 50+ libraries (10MB+ core) |
| **Setup Time** | Add dependency, start coding | Learn routes, endpoints, DSL |
| **Workflow Focus** | Pure orchestration patterns | Integration + orchestration |
| **JSON Configuration** | Native DAG support | YAML/XML routes |
| **Custom Logic** | Write Java actions | Processors + beans |

### When Apache Camel is Better:

✅ **Use Apache Camel when you need:**
- **Built-in Connectors**: 300+ components (HTTP, JMS, Database, FTP, etc.)
- **Message Transformation**: Built-in data format conversion
- **Advanced Routing**: Content-based routing, recipient lists, dynamic routing
- **Error Handling**: Dead letter channels, redelivery policies
- **Monitoring**: JMX integration, health checks, metrics
- **Enterprise Patterns**: Aggregator, Splitter, Content Enricher, etc.

### Practical Example Comparison:

**Apache Camel Route:**
```java
from("file:input")
    .split(body().tokenize("\n"))
    .choice()
        .when(xpath("//order/type = 'premium'"))
            .to("jms:premium-queue")
        .otherwise()
            .to("jms:standard-queue")
    .end();
```

**ActionBaton Equivalent:**
```java
// You implement the file reading, parsing, and JMS logic in actions
ActionBatonBuilderFactory.getInstance()
    .getSequenceBuilder()
    .add(new ReadFileAction("input"))
    .add(new SplitLinesAction())
    .add(factory.getIteratorActionBuilder()
        .iterate(new LineIterator())
        .add(factory.getConditionalBuilder()
            .predicate(new PremiumOrderPredicate())
            .ifThen(new SendToJMSAction("premium-queue"))
            .elseThen(new SendToJMSAction("standard-queue"))
            .build())
        .build())
    .build()
    .execute(context);
```

### 🎯 Summary:
- **Apache Camel**: Full integration platform with built-in components and patterns
- **ActionBaton**: Lightweight orchestration engine where you provide the integration logic

Choose ActionBaton when you need **simple workflow orchestration** with full control over your integration code. Choose Camel when you need a **comprehensive integration platform** with built-in connectors and advanced routing.

---

## 📈 Performance FAQ: ActionBaton vs Camel

**Q: Will latency be impacted if we use the same usage as Camel without its integration features?**

**Short answer**: Typically equal or lower latency with ActionBaton. For I/O-bound workflows (HTTP/DB/Kafka), the framework overhead is dwarfed by network latency, so the difference is negligible. For CPU-bound or very short steps, ActionBaton usually wins due to less framework machinery.

**Why ActionBaton tends to be faster for the same usage:**
- **Less per-step overhead**: plain method calls + lightweight bookkeeping (results list, local context, lifecycle hooks).
- **Fewer moving parts**: no Exchange/Message creation, no global TypeConverter, fewer interceptors/route policies.
- **No infrastructure layers**: no component registry or mediation pipeline to traverse.

---

## 📊 Real-World Use Cases

### E-commerce Order Processing
Sequential validation, parallel processing, and conditional notifications.
```java
ActionBatonBuilderFactory factory = ActionBatonBuilderFactory.getInstance();

factory.getSequenceBuilder()
    .add(new ValidateOrderAction())
    .add(factory.getParallelBuilder()
        .add(new ProcessPaymentAction())
        .add(new ReserveInventoryAction())
        .add(new CalculateShippingAction())
        .build())
    .add(factory.getConditionalBuilder()
        .predicate(new AllSuccessPredicate())
        .ifThen(new ConfirmOrderAction())
        .elseThen(new HandleFailureAction())
        .build())
    .build()
    .execute(context);
```

### Microservice Data Aggregation
Declarative parallel fetching of data from multiple services.
```json
{
  "actionDagId": "FETCH_DATA_FLOW",
  "rootAction": {
    "executor": "PARALLEL", 
    "properties": { "parallel": { "threadCount": 5, "timeout": 10000 } },
    "actions": [
      { "name": "FETCH_USER_DATA" },
      { "name": "FETCH_ORDER_HISTORY" },
      { "name": "FETCH_RECOMMENDATIONS" }
    ]
  }
}
```

### System Integration Workflow
Orchestrating data transformation, API calls, and message queue publishing.
```java
ActionBatonBuilderFactory factory = ActionBatonBuilderFactory.getInstance();

factory.getSequenceBuilder()
    // Transform input data for API call
    .add(factory.getMapperExecutor()
        .mapper(new UserRequestMapper())     // Maps context data to API request format
        .action(new CallUserServiceAction()) // REST API call
        .build())
    .add(new QueryDatabaseAction())          // Database operation
    .add(factory.getParallelBuilder()
        .add(factory.getMapperExecutor()
            .mapper(new KafkaMessageMapper()) // Transform data for Kafka
            .action(new PublishToKafkaAction())
            .build())
        .add(new UpdateCacheAction())        // Cache operation
        .add(new CallNotificationAPI())     // External API
        .build())
    .add(new AuditLogAction())              // Final logging
    .build()
    .execute(context);
```

