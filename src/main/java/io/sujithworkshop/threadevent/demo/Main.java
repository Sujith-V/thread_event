package io.sujithworkshop.threadevent.demo;

import io.sujithworkshop.threadevent.listener.registry.DefaultListenerRegistry;
import io.sujithworkshop.threadevent.publisher.EventPublisher;
import io.sujithworkshop.threadevent.publisher.EventPublisherBuilder;

import java.util.logging.Logger;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		// Step 1: Create a listener registry
		DefaultListenerRegistry listenerRegistry = new DefaultListenerRegistry();

		// Step 2: Register a sample listener
		listenerRegistry.register(new SampleEventListener());

		// Step 3: Build an EventPublisher
		EventPublisher eventPublisher = new EventPublisherBuilder(listenerRegistry)
				.setLogger(Logger.getLogger(Main.class.getName()))
				.build();

		// Step 4: Create an event instance
		SampleEvent sampleEvent = new SampleEvent();

		// Step 5: Publish the event
		System.out.println("\nPublishing event at START stage...");
		eventPublisher.publish(sampleEvent);

		// Step 6: Switch to the final stage (END) and publish again
		System.out.println("\nPublishing event at END stage...");
		sampleEvent.end();
		eventPublisher.publish(sampleEvent);
	}

}