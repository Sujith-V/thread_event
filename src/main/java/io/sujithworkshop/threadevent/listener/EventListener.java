package io.sujithworkshop.threadevent.listener;

import io.sujithworkshop.threadevent.core.Event;

import java.lang.reflect.ParameterizedType;

import java.lang.reflect.ParameterizedType;

/**
 * Abstract class representing an event listener for a specific type of event.
 * Subclasses should override the event stage methods to perform actions at different stages of the event lifecycle.
 *
 * @param <E> the type of event this listener handles
 */
public abstract class EventListener<E extends Event>
{
	private final Class<E> eventType;

	public EventListener()
	{
		this.eventType = findEventType();
	}

	/**
	 * Listens for an event and delegates to the appropriate event stage method.
	 *
	 * @param event the event to listen for
	 * @throws Exception if an error occurs during event handling
	 */
	public void listen(Event event) throws Exception
	{
		if (!eventType.isInstance(event))
		{
			throw new IllegalArgumentException("Unexpected event type"); //NO I18N
		}

		E typedEvent = eventType.cast(event);

		switch (event.getEventStage())
		{
			case START:
				onStart(typedEvent);
				break;
			case PRE_PERSISTENCE:
				beforePersistence(typedEvent);
				break;
			case POST_PERSISTENCE:
				afterPersistence(typedEvent);
				break;
			case SUCCESS:
				onSuccess(typedEvent);
				break;
			case FAILED:
				onFailure(typedEvent);
				break;
			case END:
				onEnd(typedEvent);
				break;
			default:
				throw new IllegalStateException("Unexpected event stage: " + event.getEventStage()); //NO I18N
		}
	}

	/**
	 * This is the start of the event, before the bean in opened.
	 *
	 * @param event the event that has started
	 * @throws Exception if an error occurs during the event start
	 */
	protected void onStart(E event) throws Exception {}

	/**
	 * This stage occurs just before the event data is persisted within the bean.
	 *
	 * @param event the event before persistence
	 * @throws Exception if an error occurs before persistence
	 */
	protected void beforePersistence(E event) throws Exception {}

	/**
	 * This stage occurs immediately after the event data is persisted within the bean.
	 *
	 * @param event the event after persistence
	 * @throws Exception if an error occurs after persistence
	 */
	protected void afterPersistence(E event) throws Exception {}

	protected void onSuccess(E event) throws Exception {}

	protected void onFailure(E event) throws Exception {}

	/**
	 * This is the end of the event, after the bean in closed.
	 * You may move these operations to an asynchronous processing in the future.
	 *
	 * @param event the event that has ended
	 * @throws Exception if an error occurs at the end of the event
	 */
	protected void onEnd(E event) throws Exception {}

	/**
	 * Finds the event type parameter for this listener.
	 *
	 * @return the event type class
	 */
	private Class<E> findEventType()
	{
		Class<?> currentClass = getClass();
		currentClass = findParameterizedClass(currentClass);
		return (Class<E>) ((ParameterizedType) currentClass.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Finds the parameterized class in the class hierarchy.
	 *
	 * @param currentClass the current class
	 * @return the parameterized class
	 */
	private Class<?> findParameterizedClass(Class<?> currentClass)
	{
		while (!(currentClass.getGenericSuperclass() instanceof ParameterizedType))
		{
			if (currentClass.getSuperclass() == null)
			{
				throw new IllegalStateException("Unable to determine event type for listener: " + getClass().getName()); //NO I18N
			}
			currentClass = currentClass.getSuperclass();
		}

		return currentClass;
	}

	/**
	 * Gets the event type class.
	 *
	 * @return the event type class
	 */
	public final Class<E> getEventType()
	{
		return eventType;
	}

	/**
	 * Gets the name of this listener.
	 *
	 * @return the listener name
	 */
	public final String getName()
	{
		return getClass().getSimpleName();
	}

	@Override
	public String toString()
	{
		return getName();
	}
}