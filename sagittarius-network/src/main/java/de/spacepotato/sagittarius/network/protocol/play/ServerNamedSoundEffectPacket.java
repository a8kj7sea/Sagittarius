package de.spacepotato.sagittarius.network.protocol.play;

import de.spacepotato.sagittarius.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a outbound play packet sent by the server to play a named sound effect at a specific position.
 *
 * @author a8kj7sea
 * @author viva_k1ng
 * @version 1.2.3
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerNamedSoundEffectPacket extends Packet {

    /**
     * The identifier string name of the sound effect to play.
     */
    private String name;

    /**
     * The X coordinate where the sound originates.
     */
    private double x;

    /**
     * The Y coordinate where the sound originates.
     */
    private double y;

    /**
     * The Z coordinate where the sound originates.
     */
    private double z;

    /**
     * The volume level of the sound (1.0 is normal volume).
     */
    private float volume;

    /**
     * The pitch modifier byte for the sound playback.
     */
    private byte pitch;

    /**
     * Writes the packet payload data into the specified ByteBuf buffer according to protocol specification.
     *
     * @param buf The buffer to write packet data into.
     * @throws Exception If a buffer encoding error occurs.
     */
    @Override
    public void write(ByteBuf buf) throws Exception {
        writeString(buf, name);
        buf.writeInt((int) (x * 8));
        buf.writeInt((int) (y * 8));
        buf.writeInt((int) (z * 8));
        buf.writeFloat(volume);
        buf.writeByte(pitch);
    }

    /**
     * Creates a new uninitialized instance of this packet.
     *
     * @return A new {@link ServerNamedSoundEffectPacket} instance.
     */
    @Override
    public Packet createNewPacket() {
        return new ServerNamedSoundEffectPacket();
    }

    /**
     * Gets the unique packet protocol ID for this sound effect packet.
     *
     * @return The packet ID (0x29).
     */
    @Override
    public int getId() {
        return 0x29;
    }
}