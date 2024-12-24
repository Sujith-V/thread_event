package io.sujithworkshop.threadevent.listener.registry;

import io.sujithworkshop.threadevent.core.Event;
import io.sujithworkshop.threadevent.listener.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListenerRegistry implements ListenerRegistry
{
	private final Map<Class<? extends Event>, List<Class<? extends EventListener<? extends Event>>>> eventListenerClassMap;

	public DefaultListenerRegistry()
	{
		this.eventListenerClassMap = new HashMap<>();
	}

	@Override
	public void register(Class<? extends EventListener<? extends Event>> listenerClass)
	{
		try
		{
			EventListener<? extends Event> listener = listenerClass.getDeclaredConstructor().newInstance();
			Class<? extends Event> eventType = listener.getEventType();
			eventListenerClassMap.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listenerClass);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<EventListener<? extends Event>> getListenersForEvent(Class<? extends Event> eventType)
	{
		List<Class<? extends EventListener<? extends Event>>> listenerClasses = eventListenerClassMap.getOrDefault(eventType, List.of());
		List<EventListener<? extends Event>> listeners = new ArrayList<>();
		for (Class<? extends EventListener<? extends Event>> listenerClass : listenerClasses)
		{
			try
			{
				listeners.add(listenerClass.getDeclaredConstructor().newInstance());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		return listeners;
	}
}