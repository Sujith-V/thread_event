package io.sujithworkshop.threadevent.listener.registry;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;

import java.util.List;

public interface ListenerRegistry
{
	void register(Class<? extends EventListener<? extends Event>> listenerClass);

	List<EventListener<? extends Event>> getListenersForEvent(Class<? extends Event> eventType);
}