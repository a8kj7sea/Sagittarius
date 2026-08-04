package me.a8kj.sagittarius.event;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event fired when a plugin message is received from a proxy.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Getter
@RequiredArgsConstructor
public class ProxyMessageEvent implements LimboEvent {
    private final Player player;
    private final String channel;
    private final byte[] data;
}