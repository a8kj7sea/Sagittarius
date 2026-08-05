package me.a8kj.sagittarius.event.player;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import lombok.Setter;
import me.a8kj.sagittarius.event.Cancellable;

/**
 * Event fired when a player leaves the Limbo server.
 * By default, the quit message is cancelled to prevent spam in the Limbo.
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
@Getter
public class PlayerQuitEvent extends PlayerEvent implements Cancellable {
    @Setter
    private String quitMessage;
    private boolean cancelled = true;

    /**
     * Constructs a new PlayerQuitEvent.
     *
     * @param player the player who left
     * @param quitMessage the default quit message
     */
    public PlayerQuitEvent(Player player, String quitMessage) {
        super(player);
        this.quitMessage = quitMessage;
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