package me.a8kj.sagittarius.event.player;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.sagittarius.event.LimboEvent;

/**
 * Abstract base class for all player-related events.
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
@RequiredArgsConstructor
@Getter
public abstract class PlayerEvent implements LimboEvent {
    private final Player player;
}