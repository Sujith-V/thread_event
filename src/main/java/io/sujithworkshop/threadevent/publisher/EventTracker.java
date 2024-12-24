package io.sujithworkshop.threadevent.publisher;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class EventTracker
{
	private final Logger logger;

	public EventTracker(Logger logger)
	{
		this.logger = logger;
	}

	public EventContext startTracking(Event event)
	{
		return new EventContext(event);
	}

	public void completeTracking(EventContext context)
	{
		Duration processingTime = context.getProcessingDuration();
		List<EventListener<?>> processedListeners = context.getProcessedListeners();
		Event event = context.getEvent();

		logger.log(Level.INFO,
				"Event {0} (ID: {1}) summary: Total processing time: {2} ms, Listeners processed: {3}, Listener names: {4}", //NO I18N
				new Object[] {
						event.getEventName(),
						event.getEventId(),
						processingTime.toMillis(),
						processedListeners.size(),
						getCommaSeparatedListenerNames(processedListeners) }
		);
	}

	private String getCommaSeparatedListenerNames(List<EventListener<?>> listeners)
	{
		return listeners.stream()
				.map(EventListener::getName)
				.collect(Collectors.joining(", "));
	}
}