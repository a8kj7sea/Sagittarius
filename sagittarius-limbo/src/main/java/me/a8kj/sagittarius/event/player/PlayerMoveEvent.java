package me.a8kj.sagittarius.event.player;

import de.spacepotato.sagittarius.entity.Player;
import lombok.Getter;
import me.a8kj.sagittarius.event.Cancellable;

/**
 * Event fired when a player moves in the Limbo server.
 *
 * @author a8kj7sea
 * @version 1.2.5
 */
@Getter
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private final double x;
    private final double y;
    private final double z;
    private boolean cancelled = false;

    /**
     * Constructs a new PlayerMoveEvent.
     *
     * @param player the player who moved
     * @param x the new X coordinate
     * @param y the new Y coordinate
     * @param z the new Z coordinate
     */
    public PlayerMoveEvent(Player player, double x, double y, double z) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
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