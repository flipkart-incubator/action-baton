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

# Detailed Examples and Core Components

This document provides detailed code examples for implementing the core interfaces of ActionBaton. These examples show how to build custom actions, predicates, mappers, and iterators to create sophisticated workflows.

---

## 📑 Index
- [ActionExecutionContext](#-actionexecutioncontext)
- [IAction](#-iaction)
- [IPredicate](#-ipredicate)
- [IIterator](#-iiterator)
- [IMapper](#-imapper)
- [IActionOrchestrator](#-iactionorchestrator)

---

## 🧠 ActionExecutionContext

The `ActionExecutionContext` is the central state management object. You should extend it to define your own shared context type.

```java
import com.baton.entities.ActionExecutionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation using String as Workflow ID and a Map for the context.
 */
public class ActionExecutionContextImpl extends ActionExecutionContext<String, Map<String, Object>> {
    public ActionExecutionContextImpl(String workflowId) {
        super(workflowId, new HashMap<String, Object>());
    }

    @Override
    public Map<String, Object> getContext() {
        return this.context;
    }
}
```

---

## 🎯 IAction

Actions are the basic units of work. Implement `IAction` to define your business logic.

```java
import com.baton.entities.IAction;
import com.baton.entities.ActionExecutionContext;

public class PrintAction implements IAction<ActionExecutionContextImpl, Exception> {
    @Override
    public String getActionName() {
        return "PRINT_TASK";
    }

    @Override
    public void execute(ActionExecutionContextImpl context) throws Exception {
        int counter = (int) context.getContext().getOrDefault("SEQUENCE_COUNTER", 0);

        System.out.println("Executing: " + getActionName() + 
                           " | Counter: " + counter +
                           " | Thread: " + Thread.currentThread().getName());
        
        context.getContext().put("SEQUENCE_COUNTER", counter + 1);
    }
}
```

---

## ⚖️ IPredicate

Predicates are used for conditional branching in workflows.

```java
import com.baton.entities.IPredicate;

public class EvenPredicate implements IPredicate<ActionExecutionContextImpl, Exception> {
    @Override
    public boolean verify(ActionExecutionContextImpl context) {
        int counter = (int) context.getContext().getOrDefault("SEQUENCE_COUNTER", 0);
        return (counter % 2 == 0);
    }

    @Override
    public String getPredicateName() {
        return "IS_EVEN_PREDICATE";
    }
}
```

---

## 🔄 IIterator

Iterators generate a list of items to be processed by an action.

```java
import com.baton.entities.IIterator;
import java.util.List;

public class NumberIterator implements IIterator<ActionExecutionContextImpl, Integer> {
    @Override
    public List<Integer> getIterator(ActionExecutionContextImpl context) {
        // Return a list of numbers to process
        return List.of(1, 2, 3, 4, 5);
    }

    @Override
    public String getName() {
        return "NUMBER_ITERATOR";
    }
}
```

---

## 🗺️ IMapper

Mappers allow you to decouple your actions from the main context by transforming data into specific objects.

```java
import com.baton.entities.IMapper;
import com.baton.commons.utils.ActionBatonConstants;

public class OrderMapper implements IMapper<ActionExecutionContextImpl, Order, Exception> {
    @Override
    public Order mapInput(ActionExecutionContextImpl context) {
        // Extract data from context and create an Order object
        String id = (String) context.getContext().get("ORDER_ID");
        return new Order(id);
    }

    @Override
    public void mapOutput(ActionExecutionContextImpl context) {
        // Optionally process output stored in localContext
        String key = ActionBatonConstants.getOutputMapperKey("PROCESS_ORDER_ACTION");
        Object result = context.getLocalContext().get(key);
        System.out.println("Processing result: " + result);
    }

    @Override
    public String getMapperName() {
        return "ORDER_MAPPER";
    }
}
```

---

## 🎭 IActionOrchestrator

The orchestrator is used to resolve components by name, which is essential for JSON-driven workflows.

```java
import com.baton.orchestrator.impl.BaseActionOrchestrator;
import com.baton.entities.*;

public class MyOrchestrator extends BaseActionOrchestrator {
    @Override
    public IAction getAction(String actionName) {
        return switch (actionName) {
            case "PRINT_TASK" -> new PrintAction();
            case "LOG_TASK" -> new LogAction();
            default -> null;
        };
    }

    @Override
    public IPredicate getPredicate(String predicateName) {
        if ("IS_EVEN".equals(predicateName)) return new EvenPredicate();
        return null;
    }

    @Override
    public IIterator getIterator(String iteratorName) {
        if ("NUMBERS".equals(iteratorName)) return new NumberIterator();
        return null;
    }

    @Override
    public IMapper getMapper(String mapperName) {
        if ("ORDER_MAPPER".equals(mapperName)) return new OrderMapper();
        return null;
    }
}
```

