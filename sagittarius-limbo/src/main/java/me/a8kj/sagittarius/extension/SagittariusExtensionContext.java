package me.a8kj.sagittarius.extension;

import de.spacepotato.sagittarius.Sagittarius;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.sagittarius.event.EventBus;
import me.a8kj.sagittarius.extension.actions.ExtensionActions;
import org.slf4j.Logger;

/**
 * Implementation of {@link ExtensionContext} providing access to core components.
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


}