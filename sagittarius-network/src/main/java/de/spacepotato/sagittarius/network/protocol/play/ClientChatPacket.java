package de.spacepotato.sagittarius.network.protocol.play;

import de.spacepotato.sagittarius.network.handler.ChildNetworkHandler;
import de.spacepotato.sagittarius.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

/**
 * Represents an incoming client chat packet sent by a player.
 *
 * @author a8kj7sea
 * @version 1.2.4
 */
@Getter
public class ClientChatPacket extends Packet {

    /**
     * The raw chat message content sent by the client.
     */
    private String message;

    /**
     * Reads the packet payload from the provided buffer to populate the chat message.
     *
     * @param buf the buffer containing the incoming packet data
     * @throws Exception if an error occurs while reading from the buffer
     */
    @Override
    public void read(ByteBuf buf) throws Exception {
        this.message = readString(buf);
    }

    /**
     * Passes this packet to the network handler for processing chat logic.
     *
     * @param childHandler the network handler responsible for processing client actions
     */
    @Override
    public void handle(ChildNetworkHandler childHandler) {
        childHandler.handleChat(this);
    }

    /**
     * Creates a new instance of this packet type.
     *
     * @return a fresh {@link ClientChatPacket} instance
     */
    @Override
    public Packet createNewPacket() {
        return new ClientChatPacket();
    }

    /**
     * Gets the unique protocol ID assigned to this packet.
     *
     * @return the packet ID ({@code 0x01})
     */
    @Override
    public int getId() {
        return 0x01;
    }
}