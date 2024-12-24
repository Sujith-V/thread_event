package io.sujithworkshop.threadevent.listener.registry;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;

import java.util.Collections;
import java.util.List;

public class UnmodifiableListenerRegistry implements ListenerRegistry
{
	private final ListenerRegistry listenerRegistry;

	public UnmodifiableListenerRegistry(ListenerRegistry listenerRegistry)
	{
		this.listenerRegistry = listenerRegistry;
	}

	@Override
	public void register(Class<? extends EventListener<? extends Event>> listenerClass)
	{
		throw new UnsupportedOperationException("Cannot register new listeners to an unmodifiable registry"); //NO I18N
	}

	@Override
	public List<EventListener<? extends Event>> getListenersForEvent(Class<? extends Event> eventType)
	{
		return Collections.unmodifiableList(listenerRegistry.getListenersForEvent(eventType));
	}
}