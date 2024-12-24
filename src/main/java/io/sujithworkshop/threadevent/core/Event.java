package io.sujithworkshop.threadevent.core;

import java.util.UUID;

public abstract class Event implements EventLifeCycle
{
	private EventStage eventStage;
	private final UUID eventId;
	private final long timestamp;

	public Event()
	{
		this(EventStage.START);
	}

	public Event(EventStage eventStage)
	{
		this.eventStage = eventStage;
		this.eventId = UUID.randomUUID();
		this.timestamp = System.currentTimeMillis();
	}

	public EventStage getEventStage()
	{
		return eventStage;
	}

	public void setEventStage(EventStage eventStage)
	{
		this.eventStage = eventStage;
	}

	public UUID getEventId()
	{
		return eventId;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public String getEventName()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public String toString()
	{
		return getEventName();
	}

	@Override
	public void start() throws Exception
	{
		setEventStage(EventStage.START);
	}

	@Override
	public void prePersist() throws Exception
	{
		setEventStage(EventStage.PRE_PERSISTENCE);
	}

	@Override
	public void postPersist() throws Exception
	{
		setEventStage(EventStage.POST_PERSISTENCE);
	}

	@Override
	public void success() throws Exception
	{
		setEventStage(EventStage.SUCCESS);
	}

	@Override
	public void fail() throws Exception
	{
		setEventStage(EventStage.FAILED);
	}

	@Override
	public void end() throws Exception
	{
		setEventStage(EventStage.END);
	}
}