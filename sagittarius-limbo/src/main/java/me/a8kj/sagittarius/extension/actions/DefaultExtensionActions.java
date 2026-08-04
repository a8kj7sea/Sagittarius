package me.a8kj.sagittarius.extension.actions;

import de.spacepotato.sagittarius.chat.ChatComponent;
import de.spacepotato.sagittarius.chat.ChatPosition;
import de.spacepotato.sagittarius.entity.Player;
import de.spacepotato.sagittarius.entity.PlayerImpl;
import de.spacepotato.sagittarius.network.protocol.play.ServerChatMessagePacket;
import de.spacepotato.sagittarius.network.protocol.play.ServerPluginMessagePacket;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Default implementation of {@link ExtensionActions} providing common actions.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Slf4j
public class DefaultExtensionActions implements ExtensionActions {

    /**
     * Transfers a player to a target server via BungeeCord plugin messaging.
     *
     * @param player the player to transfer
     * @param targetServer the target server name
     */
    @Override
    public void transferPlayer(Player player, String targetServer) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF("Connect");
            dos.writeUTF(targetServer);

            ServerPluginMessagePacket connectPacket = new ServerPluginMessagePacket("BungeeCord", out.toByteArray());
            ((PlayerImpl) player).sendPacket(connectPacket);
        } catch (Exception e) {
            log.error("Failed to transfer player {} to {}", player.getName(), targetServer, e);
        }
    }

    /**
     * Sends a chat message to a player.
     *
     * @param player the player to send the message to
     * @param message the message to send
     */
    @Override
    public void sendMessage(Player player, String message) {
        try {
            String json = new ChatComponent(message).toJson();
            ServerChatMessagePacket packet = new ServerChatMessagePacket(json, (byte) ChatPosition.SYSTEM.ordinal());
            ((PlayerImpl) player).sendPacket(packet);
        } catch (Exception e) {
            log.error("Failed to send message to player {}", player.getName(), e);
        }
    }
}