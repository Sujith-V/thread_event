package io.sujithworkshop.threadevent.publisher;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;
import io.sujithworkshop.threadevent.listener.exception.ListenerExceptionHandler;
import io.sujithworkshop.threadevent.listener.registry.ListenerRegistry;
import io.sujithworkshop.threadevent.listener.registry.UnmodifiableListenerRegistry;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EventPublisher
{
	private final ListenerRegistry listenerRegistry;
	private final ListenerExceptionHandler exceptionHandler;
	private final EventTracker eventTracker;
	private final Logger logger;

	public EventPublisher(ListenerRegistry listenerRegistry, ListenerExceptionHandler exceptionHandler, Logger logger)
	{
		this.listenerRegistry = new UnmodifiableListenerRegistry(listenerRegistry);
		this.exceptionHandler = exceptionHandler;
		this.logger = logger;
		this.eventTracker = new EventTracker(logger);
	}

	public <E extends Event> void publish(E event) throws Exception
	{
		publish(event, exceptionHandler);
	}

	public <E extends Event> void publish(E event, ListenerExceptionHandler exceptionHandler) throws Exception
	{
		String eventName = event.getEventName();
		UUID eventId = event.getEventId();

		logger.log(Level.INFO, "Publishing event: {0} (ID: {1}) with stage: {2}", new Object[] { eventName, eventId, event.getEventStage() }); //NO I18N

		Class<? extends Event> eventType = event.getClass();
		List<EventListener<? extends Event>> eventListeners = listenerRegistry.getListenersForEvent(eventType);

		logger.log(Level.INFO, "Found {0} listeners for event {1} (ID: {2})", new Object[]{ eventListeners.size(), eventName, eventId }); //NO I18N

		// Track event processing
		EventContext context = eventTracker.startTracking(event);

		for (EventListener<?> listener : eventListeners)
		{
			String listenerName = listener.getName();
			try
			{
				logger.log(Level.INFO, "Invoking listener: {0} for event {1} (ID: {2})", new Object[]{ listenerName, eventName, eventId }); //NO I18N
				listener.listen(event);
				logger.log(Level.INFO, "Listener {0} processed event {1} (ID: {2}) successfully", new Object[] { listenerName, eventName, eventId }); //NO I18N

				context.recordListenerProcessing(listener);
			}
			catch (Exception e)
			{
				logger.log(Level.INFO, "Listener {0} threw an exception in the event {1} (ID: {2}) and so calling the exception handler {3}", new Object[] { listenerName, eventName, eventId, exceptionHandler.toString(), e });
				exceptionHandler.handleListenerException(event, listener, e);
			}
		}

		eventTracker.completeTracking(context);

		logger.log(Level.INFO, "Event {0} (ID: {1}) processing completed", new Object[] { eventName, eventId } );
	}
}