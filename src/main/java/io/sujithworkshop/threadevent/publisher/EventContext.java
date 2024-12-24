package io.sujithworkshop.threadevent.publisher;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class EventContext
{
	private final Event event;
	private final Instant startTime;
	private final List<EventListener<?>> processedListeners = new ArrayList<>();

	public EventContext(Event event)
	{
		this.event = event;
		this.startTime = Instant.ofEpochMilli(event.getTimestamp());
	}

	public void recordListenerProcessing(EventListener<?> listener)
	{
		processedListeners.add(listener);
	}

	public Event getEvent()
	{
		return event;
	}

	public Duration getProcessingDuration()
	{
		Instant endTime = Instant.now();
		return Duration.between(startTime, endTime);
	}

	public List<EventListener<?>> getProcessedListeners()
	{
		return Collections.unmodifiableList(processedListeners);
	}
}