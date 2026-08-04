package me.a8kj.sagittarius.extension;

import de.spacepotato.sagittarius.Sagittarius;
import me.a8kj.sagittarius.event.EventBus;
import me.a8kj.sagittarius.extension.actions.ExtensionActions;
import org.slf4j.Logger;

/**
 * Provides context for an extension, giving access to core server components.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
public interface ExtensionContext {
    /**
     * Gets the main server instance.
     *
     * @return the server instance
     */
    Sagittarius getServer();

    /**
     * Gets the event bus for subscribing to events.
     *
     * @return the event bus
     */
    EventBus getEventBus();

    /**
     * Gets the logger for logging messages.
     *
     * @return the logger
     */
    Logger getLogger();

    /**
     * Gets the default extension actions.
     *
     * @return the default actions
     */
    ExtensionActions getDefaultActions();
}