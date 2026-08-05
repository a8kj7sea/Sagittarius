package me.a8kj.sagittarius.event.player;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import me.a8kj.sagittarius.event.Cancellable;

/**
 * Event fired when a player sends a chat message.
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
@Getter
public class PlayerChatEvent extends PlayerEvent implements Cancellable {
    private final String message;
    private boolean cancelled = false;

    /**
     * Constructs a new PlayerChatEvent.
     *
     * @param player the player who sent the message
     * @param message the content of the chat message
     */
    public PlayerChatEvent(Player player, String message) {
        super(player);
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}