package me.a8kj.sagittarius.event;

/**
 * Interface for events that can be cancelled (e.g., preventing a message from being sent).
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
public interface Cancellable {
    /**
     * Checks if the event is cancelled.
     *
     * @return true if the event is cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * Sets the cancelled state of the event.
     *
     * @param cancel true to cancel the event, false to allow it
     */
    void setCancelled(boolean cancel);
}