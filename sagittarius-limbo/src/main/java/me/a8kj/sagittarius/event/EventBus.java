package me.a8kj.sagittarius.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * A simple event bus that manages event subscriptions and publications.
 * It allows extensions to subscribe to specific event types and publish events
 * to all registered listeners.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
public class EventBus {
    private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    /**
     * Subscribes a listener to a specific event type.
     *
     * @param <T> the type of the event
     * @param eventType the class of the event
     * @param listener the consumer that will handle the event
     */
    public <T extends LimboEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Publishes an event to all subscribed listeners.
     *
     * @param <T> the type of the event
     * @param event the event instance to publish
     */
    @SuppressWarnings("unchecked")
    public <T extends LimboEvent> void publish(T event) {
        List<Consumer<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) return;

        for (Consumer<?> listener : eventListeners) {
            try {
                ((Consumer<T>) listener).accept(event);
            } catch (Throwable t) {
                System.err.println("Error processing event " + event.getClass().getSimpleName() + ": " + t.getMessage());
            }
        }
    }
}