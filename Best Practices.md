# Best Practices for Using Threadevent Framework

This document provides a comprehensive guide to the dos and don’ts of working with the **Threadevent Framework**. Following these guidelines will help you create robust, maintainable, and scalable event-driven systems.

---

## Event Stages and Best Practices

The Threadevent Framework organizes event processing into various lifecycle stages. Each stage has a specific purpose and guidelines for optimal use.

---

### **Start Stage**

The **Start** stage is the entry point of the event lifecycle. It is critical for initializing and preparing the event.

#### Recommended Tasks:

- Initialize required event data.
- Perform validations to ensure the event is suitable for processing.
- Derive and enrich event-related details necessary for subsequent stages.

#### Example:

```java
@Override
protected void onStart(MyEvent event) {
    // Validate input data
    if (event.getData() == null) {
        throw new IllegalArgumentException("Event data cannot be null");
    }

    // Derive additional details
    event.setDerivedValue(computeDerivedValue(event.getData()));
}
```

#### Avoid:

- Performing database (write) operations, as this stage is meant to execute before the bean.

---

### **Before Persistence Stage**

The **Before Persistence** stage allows you to perform actions required before persisting the event data.

#### Recommended Tasks:

- Handle operations that cannot be performed during the Start stage.
- Execute final checks or preparations before the event is saved.

#### Example:

```java
@Override
protected void beforePersistence(MyEvent event) {
    // Ensure all required fields are populated
    if (event.getDerivedValue() == null) {
        throw new IllegalStateException("Derived value is missing");
    }
}
```

#### Avoid:

- Repeating validations or derivations already handled in the **Start** stage.

---

### **After Persistence Stage**

The **After Persistence** stage is used for tail operations closely tied to the entity's persistence.

#### Recommended Tasks:

- Execute only operations that must succeed with the entity's persistence.
- Keep the logic here as lightweight as possible to reduce rollback risks.
- Log and capture any failures for debugging.

#### Example:

```java
@Override
protected void afterPersistence(MyEvent event) {
    calculator.updateDerivedAmounts(event);
}
```

#### Important Notes:

- If a failure occurs in this stage, the event will be rolled back.
- Move non-critical operations to **End**, **Success**, or **Failure** stages to improve performance as this stage holds an open database instance (bean).

---

### **Success Stage**

The **Success** stage handles actions after the event has been successfully processed. It is ideal for follow-up tasks that signify the event’s success.

#### Recommended Tasks:

- Send notifications (e.g., email, push alerts).
- Trigger downstream workflows or updates in dependent systems.
- Log success metrics for observability.

#### Example:

```java
@Override
protected void onSuccess(MyEvent event) {
    // Log success
    logger.info("Event processed successfully: " + event.getEventName());

	// Notify downstream systems about the new state
	notificationService.notifyUpdate(event.getEntityId());
}
```

#### Avoid:

- Holding or modifying the persisted entity instance.

#### Best Practices:

- Gradually move operations from **After Persistence** to **Success** to improve performance.

---

### **Failure Stage**

The **Failure** stage is invoked when event processing fails. It allows for compensating, fallback, or recovery mechanisms.

#### Recommended Tasks:

- Log failure metrics and error details for debugging.
- Notify relevant stakeholders or systems about the failure.
- Trigger compensating actions or clean-ups.
- Ensure appropriate mechanisms are in place to revert partial changes.

#### Example:

```java
@Override
protected void onFailure(MyEvent event, Throwable cause) {
    // Log the error
    logger.error("Event processing failed: " + event.getEventName(), cause);

    // Notify stakeholders
    notificationService.notifyFailure(event.getEntityId(), cause);
}
```

#### Avoid:

- Introducing heavy recovery logic; delegate to asynchronous mechanisms if needed.

---

### **End Stage**

The **End** stage is for final operations that do not depend on the entity's database instance. It is ideal for tasks that can be deferred or performed asynchronously.

#### Recommended Tasks:

- Clean up resources or temporary data.
- Perform asynchronous tasks like reporting or analytics.

#### Example:

```java
@Override
protected void onEnd(MyEvent event) {
    // Cleanup temporary files
    fileService.cleanupTemporaryFiles(event.getTempFileId());

    // Log completion
    logger.info("Event lifecycle ended: " + event.getEventName());
}
```

#### Best Practices:

- Prioritize asynchronous execution for non-critical tasks.

---

## General Guidelines

### Do's

1. **Keep Event Data Updated**: Always update event details before setting the next stage to ensure listeners receive the latest information.
2. **Trigger from One Dedicated Point**: Ensure each stage in the event lifecycle is triggered from only one point to maintain a well-defined API and avoid redundancy.
3. **Create Dedicated Listener Classes**: Use separate listener classes for each listening module and operation type to keep them decoupled and maintainable.
4. **Move to Asynchronous Operations**: Gradually migrate operations from **After Persistence** to non-bean stages (**Success**, **Failure**, and **End**) for non-critical follow-ups, and later to asynchronous tasks (e.g., Kafka, ZQueue) for scalability.

### Don'ts

1. **Avoid Data Transfer via Event Variables**: Do not pass data between stages using event variables to avoid unintended dependencies.
2. **No DB Writes in Start Stage**: Avoid database (write) operations in the **Start** stage. Use **Before Persistence** for such operations.
3. **Avoid Misusing Stages**: Use stages for their intended purpose and avoid mixing responsibilities to maintain clarity.
4. **Prevent Silent Failures**: Always handle exceptions and log errors to prevent hidden issues.
5. **Avoid Long-Running Tasks in Bean Stages**: Delegate time-consuming operations to asynchronous or non-bean stages for better performance.

---

## Summary

By adhering to these stage-specific best practices, you can:

1. Ensure clear separation of concerns.
2. Maintain scalability and observability.
3. Build robust and maintainable event-driven applications.

Implement these best practices to harness the full potential of the Threadevent Framework and build resilient event-driven architectures!

