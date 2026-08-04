package me.a8kj.sagittarius.extension;

import de.spacepotato.sagittarius.Sagittarius;
import de.spacepotato.sagittarius.command.Command;
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
     * Registers a command with the server.
     *
     * @param command the command to register
     *
     * @author a8kj7sea
     * @version 1.2.4
     */
    void registerCommand(Command command);
}