package me.a8kj.sagittarius.extension.actions;

import de.spacepotato.sagittarius.entity.Player;

/**
 * Interface defining actions that can be performed by extensions.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
public interface ExtensionActions {
    /**
     * Transfers a player to a target server.
     *
     * @param player the player to transfer
     * @param targetServer the target server name
     */
    void transferPlayer(Player player, String targetServer);

    /**
     * Sends a message to a player.
     *
     * @param player the player to send the message to
     * @param message the message to send
     */
    void sendMessage(Player player, String message);
}