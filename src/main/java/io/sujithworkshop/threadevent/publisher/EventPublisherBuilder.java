package io.sujithworkshop.threadevent.publisher;

import io.sujithworkshop.threadevent.listener.exception.DefaultListenerExceptionHandler;
import io.sujithworkshop.threadevent.listener.exception.ListenerExceptionHandler;
import io.sujithworkshop.threadevent.listener.registry.ListenerRegistry;

import java.util.logging.Logger;

public class EventPublisherBuilder
{
	private final ListenerRegistry listenerRegistry;
	private ListenerExceptionHandler exceptionHandler;
	private Logger logger;

	public EventPublisherBuilder(ListenerRegistry listenerRegistry)
	{
		this.listenerRegistry = listenerRegistry;
	}

	public EventPublisherBuilder setExceptionHandler(ListenerExceptionHandler exceptionHandler)
	{
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	public EventPublisherBuilder setLogger(Logger logger)
	{
		this.logger = logger;
		return this;
	}

	public EventPublisher build()
	{
		if (logger == null)
		{
			logger = Logger.getLogger(EventPublisher.class.getSimpleName());
		}
		if (exceptionHandler == null)
		{
			exceptionHandler = new DefaultListenerExceptionHandler(logger);
		}
		return new EventPublisher(listenerRegistry, exceptionHandler, logger);
	}
}