package de.spacepotato.sagittarius.network.handler;

import de.spacepotato.sagittarius.network.protocol.Packet;
import de.spacepotato.sagittarius.network.protocol.State;
import de.spacepotato.sagittarius.network.protocol.handshake.ClientHandshakePacket;
import de.spacepotato.sagittarius.network.protocol.login.ClientLoginStartPacket;
import de.spacepotato.sagittarius.network.protocol.mappings.PacketMappings;
import de.spacepotato.sagittarius.network.protocol.mappings.PacketRegistry;
import de.spacepotato.sagittarius.network.protocol.play.*;
import de.spacepotato.sagittarius.network.protocol.status.ClientStatusPingPacket;
import de.spacepotato.sagittarius.network.protocol.status.ClientStatusRequestPacket;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ChildNetworkHandler {

	protected final Channel channel;
	@Getter
	protected State state;
	protected PacketMappings stateMappings;
	
	protected ChildNetworkHandler(Channel channel) {
		this.channel = channel;
		setState(State.HANDSHAKE);
	}
	
	protected void setState(State state) {
		this.state = state;
		this.stateMappings = PacketRegistry.getPackets(state);
	}
	
	public Packet createPacket(int id) {
		return stateMappings.createPacket(id);
	}

	public void handleError(Throwable throwable) {
		if (channel.isOpen()) {
			channel.close();
		}
		if (throwable instanceof java.io.IOException ||
				throwable instanceof io.netty.handler.codec.DecoderException ||
				throwable instanceof io.netty.handler.timeout.ReadTimeoutException) {
			log.debug("Connection error ({}): {}", channel.remoteAddress(), throwable.getMessage());
		} else {
			log.error("Exception in connection pipeline ({})", channel.remoteAddress(), throwable);
		}
	}

	public void handleDisconnect() {
		
	}

	public abstract void handleHandshake(ClientHandshakePacket packet);

	public abstract void handleStatusRequest(ClientStatusRequestPacket packet);

	public abstract void handleStatusPing(ClientStatusPingPacket packet);

	public abstract void handleLoginStart(ClientLoginStartPacket packet);

	public abstract void handleKeepAlive(ClientKeepAlivePacket packet);

	public abstract void handleClientSettings(ClientSettingsPacket packet);

	public abstract void handlePosition(ClientPositionPacket packet);

	public abstract void handleLook(ClientLookPacket packet);

	public abstract void handlePositionLook(ClientPositionLookPacket packet);

	public abstract void handlePluginMessage(ClientPluginMessagePacket packet);
}
