package io.sujithworkshop.threadevent.demo;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.core.EventStage;

// Example of a custom event
class SampleEvent extends Event
{
	public SampleEvent()
	{
		super(EventStage.START);
	}

	@Override
	public String toString()
	{
		return "SampleEvent with ID: " + getEventId();
	}
}
