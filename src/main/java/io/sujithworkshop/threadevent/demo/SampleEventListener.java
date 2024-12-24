package io.sujithworkshop.threadevent.demo;

import io.sujithworkshop.threadevent.listener.EventListener;

// Example of a listener for the custom event
class SampleEventListener extends EventListener<SampleEvent>
{
	@Override
	protected void onStart(SampleEvent event) throws Exception
	{
		System.out.println("SampleEventListener handling START stage for " + event);
	}

	@Override
	protected void onEnd(SampleEvent event) throws Exception
	{
		System.out.println("SampleEventListener handling END stage for " + event);
	}
}
