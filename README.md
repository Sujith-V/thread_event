# Threadevent Framework

Threadevent is a lightweight, synchronous event management framework inspired by Guava's EventBus. It simplifies synchronous event handling while maintaining clean separation between events and listener logics. This guide outlines the framework's features, usage, best practices, and advice to help you leverage it effectively.

---

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Core Components](#core-components)
4. [Installation](#installation)
5. [Sample Usage](#sample-usage)
6. [Contributing](#contributing)
7. [License](#license)
8. [Roadmap](#roadmap)

---

## Overview

Threadevent simplifies event-driven architecture by enabling developers to:
- Define custom events.
- Implement event lifecycle methods.
- Register listeners for events.
- Publish events and handle them efficiently.

This framework is ideal for projects that require clear separation of concerns and a robust mechanism for event handling. Threadevent provides precise visibility into the entity, event, and stage being worked on, ensuring operations are triggered exactly when expected. This makes event handling intuitive, reliable, and predictable.

### What makes Threadevent Unique?

Threadevent builds on traditional pub-sub model but introduces several intuitive features that make it stand out:

- **Structured Event Lifecycle**: Threadevent organizes events into well-defined lifecycle stages like Start, Before Persistence, After Persistence, Success, Failure, and End. Each stage has a specific purpose, ensuring clarity, clear separation of concerns and organized event handling.

- **Stage-Specific Execution**: Operations are executed precisely at the right stage, ensuring predictable and error-free event handling.

- **Transparent Stage Processing**: Gain clear visibility into the entity, event, and stage you're working on, making the event processing flow transparent and easy to follow.

- **Developer-Friendly API**: Intuitive hooks for each stage reduce boilerplate code and enable modular, maintainable design.

- **Flexible Error Handling**: Supports both global and event-specific error handlers. A dedicated error handler passed during event publishing overrides the global handler, offering fine-grained control for individual events.

---

## Features

- **Synchronous Event Processing**: Handles events and listeners in a synchronous manner for predictable execution.
- **Event Lifecycle Management**: Supports multiple stages including `Start`, `Pre Persistence`, `Post Persistence`, `Success`, `Failed`, and `End`.
- **Custom Listeners**: Implement specialized listeners for specific events.
- **Robust Exception Handling**: Allows custom exception handling strategies for the exceptions thrown from listeners.
- **Event Tracking**: Tracks event processing details, such as processing time and listeners involved.
- **Unmodifiable Listener Registry**: Prevents modifications to the listener registry after setup.
- **Extendable Design**: Easily extend or adapt the framework to suit your needs.

---

## Core Components

### 1. **Event**
`Event` represents the core entity in the framework. It supports multiple lifecycle stages to allow precise control over event handling such as,
- `START`
- `PRE_PERSISTENCE`
- `POST_PERSISTENCE`
- `SUCCESS`
- `FAILED`
- `END`

#### Example
```java
public class MyEvent extends Event {
    // Your fields and methods
}
```

---

### 2. **EventListener**
`EventListener` is an abstract class that listens to specific events. Override lifecycle methods like `onStart`, `onEnd`, etc., to handle different stages.

#### Example
```java
public class MyEventListener extends EventListener<MyEvent> {
    @Override
    protected void onStart(MyEvent event) {
        System.out.println("Event started: " + event.getEventName());
    }

   @Override
   protected void onEnd(MyEvent event) {
      System.out.println("Event ended: " + event.getEventName());
   }
}
```

---

### 3. **EventPublisher**
`EventPublisher` is responsible for publishing events and invoking corresponding listeners.

#### Example
```java
ListenerRegistry registry = new DefaultListenerRegistry();
registry.register(MyEventListener.class);

EventPublisher publisher = new EventPublisherBuilder(registry).build();

MyEvent event = new MyEvent();
publisher.publish(event);
```

---

### 4. **ListenerRegistry**
`ListenerRegistry` manages the association between events and their listeners.
- **DefaultListenerRegistry**: A mutable registry for development.
- **UnmodifiableListenerRegistry**: Wraps a registry to make it immutable.

---

### 5. **EventTracker**
`EventTracker` provides insights into event processing with metrics such as total processing time and the list of processed listeners.

---

### 6. **Exception Handling**
`ListenerExceptionHandler` provides a mechanism to handle listener exceptions.
- **DefaultListenerExceptionHandler** logs exceptions and propagates them.

#### Example
```java
ListenerExceptionHandler handler = new DefaultListenerExceptionHandler(Logger.getLogger("EventLogger"));
```

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/sujith-workshop/thread-event.git
   ```
2. Navigate to the project directory:
   ```bash
   cd thread-event
   ```
3. Add the framework to your project as a dependency.

---

## Sample Usage

### Define an Event
```java
public class InvoiceCreateEvent extends Event {
	private final String invoiceId;

	public InvoiceCreateEvent(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}
}
```

### Create a Listener
```java
public class InvoiceCreateListener extends EventListener<InvoiceEvent> {
	@Override
	protected void onStart(InvoiceEvent event) {
		System.out.println("Processing invoice: " + event.getInvoiceId());
	}

	@Override
	protected void onEnd(InvoiceEvent event) {
		System.out.println("Invoice processed: " + event.getInvoiceId());
	}
}
```

### Publish an Event
```java
public class Main {
	public static void main(String[] args) throws Exception {
		ListenerRegistry registry = new DefaultListenerRegistry();
		registry.register(InvoiceCreateListener.class);

		EventPublisher publisher = new EventPublisherBuilder(registry).build();

		InvoiceCreateEvent event = new InvoiceCreateEvent("INV-12345");
		publisher.publish(event);
	}
}
```

---

## Contributing
We welcome contributions to enhance Threadevent! Follow these steps:

1. Fork the repository.
2. Create a feature branch.
3. Submit a merge request describing your changes.

---

## Roadmap
### Current Features:
- Synchronous event lifecycle processing.
- Listener exception handling.
- Immutable listener registries for production use.

### Planned Enhancements:
- **Asynchronous Event Handling**: Enable asynchronous listener execution by pushing the event to ZQueue at the end (onSuccess()) of the event processing.

Stay tuned for updates!
