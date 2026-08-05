package me.a8kj.sagittarius.event.player;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import lombok.Setter;
import me.a8kj.sagittarius.event.Cancellable;

/**
 * Event fired when a player joins the Limbo server.
 * By default, the join message is cancelled to prevent spam in the Limbo.
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
@Getter
public class PlayerJoinEvent extends PlayerEvent implements Cancellable {
    @Setter
    private String joinMessage;
    private boolean cancelled = true;

    /**
     * Constructs a new PlayerJoinEvent.
     *
     * @param player the player who joined
     * @param joinMessage the default join message
     */
    public PlayerJoinEvent(Player player, String joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
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