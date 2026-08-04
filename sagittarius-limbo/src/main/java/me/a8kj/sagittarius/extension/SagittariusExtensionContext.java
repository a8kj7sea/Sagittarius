package me.a8kj.sagittarius.extension;

import de.spacepotato.sagittarius.Sagittarius;
import de.spacepotato.sagittarius.SagittariusImpl;
import de.spacepotato.sagittarius.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.sagittarius.event.EventBus;
import me.a8kj.sagittarius.extension.actions.ExtensionActions;
import org.slf4j.Logger;

/**
 * Implementation of {@link ExtensionContext} that manages server components
 * and handles command registration for an extension.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@RequiredArgsConstructor
@Getter
public class SagittariusExtensionContext implements ExtensionContext {
    private final Sagittarius server;
    private final EventBus eventBus;
    private final Logger logger;
    private final ExtensionActions defaultActions;


    /**
     * {@inheritDoc}
     * <p>
     * Casts the server instance to {@link SagittariusImpl} and registers
     * the command using its internal command handler.
     *
     * @param command the command to register
     *
     * @author a8kj7sea
     * @version 1.2.4
     */
    @Override
    public void registerCommand(Command command) {
        ((SagittariusImpl) server).getCommandHandler().registerCommand(command);
    }
}