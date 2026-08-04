package de.spacepotato.sagittarius.network.protocol.play;

import de.spacepotato.sagittarius.network.handler.ChildNetworkHandler;
import de.spacepotato.sagittarius.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import lombok.Getter;

/**
 * Represents a client-bound plugin message packet used in the 1.8 protocol.
 * This packet is sent by the client (or proxy) to the server to transfer custom data.
 *
 * @author a8kj7sea
 * @version 1.2.3
 */
@Getter
public class ClientPluginMessagePacket extends Packet {

    private String channel;
    private byte[] data;

    /**
     * Reads the packet data from the given ByteBuf.
     * In the 1.8 protocol, the remaining bytes of the buffer are read as the data payload.
     *
     * @param buf the ByteBuf to read from
     * @throws Exception if an error occurs during reading
     */
    @Override
    public void read(ByteBuf buf) throws Exception {
        this.channel = readString(buf);
        this.data = new byte[buf.readableBytes()];
        buf.readBytes(data);
    }

    /**
     * Handles the packet by passing it to the appropriate method in the child network handler.
     *
     * @param childHandler the child network handler
     */
    @Override
    public void handle(ChildNetworkHandler childHandler) {
        childHandler.handlePluginMessage(this);
    }

    /**
     * Creates and returns a new instance of this packet.
     *
     * @return a new ClientPluginMessagePacket instance
     */
    @Override
    public Packet createNewPacket() {
        return new ClientPluginMessagePacket();
    }

    /**
     * Returns the unique identifier for this packet.
     *
     * @return the packet id
     */
    @Override
    public int getId() {
        return 0x17;
    }
}