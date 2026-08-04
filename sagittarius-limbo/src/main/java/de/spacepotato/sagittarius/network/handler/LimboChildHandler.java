package de.spacepotato.sagittarius.network.handler;

import de.spacepotato.sagittarius.GameMode;
import de.spacepotato.sagittarius.Sagittarius;
import de.spacepotato.sagittarius.SagittariusImpl;
import de.spacepotato.sagittarius.cache.PacketCache;
import de.spacepotato.sagittarius.config.LimboConfig;
import de.spacepotato.sagittarius.entity.PlayerImpl;
import de.spacepotato.sagittarius.mojang.BungeeCordGameProfile;
import de.spacepotato.sagittarius.mojang.GameProfile;
import de.spacepotato.sagittarius.mojang.OfflineGameProfile;
import de.spacepotato.sagittarius.mojang.SkinProperty;
import de.spacepotato.sagittarius.network.protocol.Packet;
import de.spacepotato.sagittarius.network.protocol.PacketContainer;
import de.spacepotato.sagittarius.network.protocol.State;
import de.spacepotato.sagittarius.network.protocol.handshake.ClientHandshakePacket;
import de.spacepotato.sagittarius.network.protocol.login.ClientLoginStartPacket;
import de.spacepotato.sagittarius.network.protocol.login.ServerLoginSuccessPacket;
import de.spacepotato.sagittarius.network.protocol.play.*;
import de.spacepotato.sagittarius.network.protocol.status.ClientStatusPingPacket;
import de.spacepotato.sagittarius.network.protocol.status.ClientStatusRequestPacket;
import de.spacepotato.sagittarius.network.protocol.status.ClientStatusResponsePacket;
import de.spacepotato.sagittarius.network.protocol.status.ServerStatusPongPacket;
import de.spacepotato.sagittarius.util.PlayerMovementTracker;
import de.spacepotato.sagittarius.world.Location;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.a8kj.sagittarius.event.ProxyMessageEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Slf4j
public class LimboChildHandler extends ChildNetworkHandler {

    private PlayerImpl player;
    private ClientHandshakePacket handshake;
    private final Queue<Integer> keepAliveIds;
    @Getter
    private PlayerMovementTracker movementTracker;

    public LimboChildHandler(Channel channel) {
        super(channel);
        keepAliveIds = new ArrayDeque<>();
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void sendPacket(PacketContainer container) {
        channel.writeAndFlush(container);
    }

    public void sendPacket(ByteBuf buf) {
        channel.writeAndFlush(buf);
    }

    public int requestKeepAlive(int keepAliveId) {
        keepAliveIds.add(keepAliveId);
        return keepAliveIds.size();
    }

    @Override
    public void handleDisconnect() {
        if (player == null) {
            return;
        }
        synchronized (Sagittarius.getInstance().getPlayers()) {
            Sagittarius.getInstance().getPlayers().remove(player);
        }
        log.info(player.getName() + " disconnected.");
    }

    // ============================================================ \\
    //                                                              \\
    //                           Handshake                          \\
    //                                                              \\
    // ============================================================ \\


    @Override
    public void handleHandshake(ClientHandshakePacket packet) {
        if (packet.getNextState() != State.LOGIN.ordinal() && packet.getNextState() != State.STATUS.ordinal()) {
            channel.close();
            return;
        }
        handshake = packet;
        setState(State.values()[packet.getNextState()]);
    }

    // ============================================================ \\
    //                                                              \\
    //                            Status                            \\
    //                                                              \\
    // ============================================================ \\


    @Override
    public void handleStatusRequest(ClientStatusRequestPacket packet) {
        LimboConfig config = Sagittarius.getInstance().getConfig();
        String json = "{\"version\":{\"name\":\"1.8.8\",\"protocol\":47},\"players\":{\"max\":" + config.getMaxPlayers() + ",\"online\":0},\"description\":{\"text\":\"" + config.getMotd() + "\"}}";
        sendPacket(new ClientStatusResponsePacket(json));
    }

    @Override
    public void handleStatusPing(ClientStatusPingPacket packet) {
        sendPacket(new ServerStatusPongPacket(packet.getPayload()));
    }

    // ============================================================ \\
    //                                                              \\
    //                             Login                            \\
    //                                                              \\
    // ============================================================ \\

    @Override
    public void handleLoginStart(ClientLoginStartPacket packet) {
        String name = packet.getName();
        LimboConfig config = Sagittarius.getInstance().getConfig();
        PacketCache cache = SagittariusImpl.getInstance().getPacketCache();

        GameProfile gameProfile;
        if (BungeeCordGameProfile.isBungeeCordForwarding(handshake)) {
            gameProfile = new BungeeCordGameProfile(name, handshake);
        } else {
            gameProfile = new OfflineGameProfile(name);
        }

        player = new PlayerImpl(this, gameProfile);
        movementTracker = new PlayerMovementTracker(player);

        // Accept the login attempt
        ServerLoginSuccessPacket success = new ServerLoginSuccessPacket(player.getUUID().toString(), player.getName());
        sendPacket(success);

        // Load PLAY-packets
        setState(State.PLAY);
        sendPacket(cache.getJoinGame());
        sendPacket(cache.getSpawnPosition());

        // Add to tablist for skin
        List<ServerPlayerListItemPacket.PlayerListEntry> entries = new ArrayList<>();
        GameMode gameMode = config.getTabGameMode().orElse(config.getGameMode());
        entries.add(new ServerPlayerListItemPacket.PlayerListEntry(player.getUUID(), player.getName(), player.getSkin().orElse(new SkinProperty[0]), gameMode.getId(), 0, null));
        ServerPlayerListItemPacket tablist = new ServerPlayerListItemPacket((byte) 0, entries);
        sendPacket(tablist);

        SagittariusImpl.getInstance().getWorldCache().send(player);

        sendPacket(cache.getPositionAndLook());
        sendPacket(cache.getPlayerAbilities());

        Location spawn = config.getSpawnPoint();
        sendPacket(new ServerNamedSoundEffectPacket("random.pop", spawn.getX(), spawn.getY(), spawn.getZ(), 1.0f, (byte) 63));


        synchronized (Sagittarius.getInstance().getPlayers()) {
            Sagittarius.getInstance().getPlayers().add(player);
        }

        if (config.shouldSendJoinMessage()) {
            player.sendMessage(config.getJoinMessage());
        }
        log.info(player.getName() + " has logged in.");
    }

    // ============================================================ \\
    //                                                              \\
    //                             Play                             \\
    //                                                              \\
    // ============================================================ \\

    @Override
    public void handleKeepAlive(ClientKeepAlivePacket packet) {
        // Keep-Alive mismatch!
        if (keepAliveIds.isEmpty() || keepAliveIds.poll() != packet.getKeepAliveId()) {
            player.kick("Invalid Keep-Alive packet received.");
        }
    }

    @Override
    public void handleClientSettings(ClientSettingsPacket packet) {
        // Properly update skin
        ServerEntityMetadataPacket metadata = new ServerEntityMetadataPacket(packet.getDisplayedSkinParts());
        sendPacket(metadata);
    }

    @Override
    public void handlePosition(ClientPositionPacket packet) {
        boolean freeze = Sagittarius.getInstance().getConfig().isCancelMove() &&
                Sagittarius.getInstance().getConfig().getGameMode() != GameMode.CREATIVE;

        if (freeze) {
            Location spawn = Sagittarius.getInstance().getConfig().getSpawnPoint();
            if (Math.abs(packet.getX() - spawn.getX()) > 0.01 ||
                    Math.abs(packet.getY() - spawn.getY()) > 0.01 ||
                    Math.abs(packet.getZ() - spawn.getZ()) > 0.01) {
                sendPacket(SagittariusImpl.getInstance().getPacketCache().getPositionAndLook());
                if (Sagittarius.getInstance().getConfig().shouldConnectOnMove()) {
                    movementTracker.tryConnect();
                }
            }
        } else {
            movementTracker.onMove(packet.getX(), packet.getY(), packet.getZ());
        }
    }

    @Override
    public void handleLook(ClientLookPacket packet) {
        movementTracker.onRotate(packet.getYaw(), packet.getPitch());
    }

    @Override
    public void handlePositionLook(ClientPositionLookPacket packet) {
        boolean freeze = Sagittarius.getInstance().getConfig().isCancelMove() &&
                Sagittarius.getInstance().getConfig().getGameMode() != GameMode.CREATIVE;

        if (freeze) {
            Location spawn = Sagittarius.getInstance().getConfig().getSpawnPoint();
            if (Math.abs(packet.getX() - spawn.getX()) > 0.01 ||
                    Math.abs(packet.getY() - spawn.getY()) > 0.01 ||
                    Math.abs(packet.getZ() - spawn.getZ()) > 0.01) {
                sendPacket(SagittariusImpl.getInstance().getPacketCache().getPositionAndLook());
                if (Sagittarius.getInstance().getConfig().shouldConnectOnMove()) {
                    movementTracker.tryConnect();
                }
            } else {
                movementTracker.onRotate(packet.getYaw(), packet.getPitch());
            }
        } else {
            movementTracker.onMove(packet.getX(), packet.getY(), packet.getZ());
            movementTracker.onRotate(packet.getYaw(), packet.getPitch());
        }
    }


    @Override
    public void handlePluginMessage(ClientPluginMessagePacket packet) {
        SagittariusImpl.getInstance().getEventBus().publish(
                new ProxyMessageEvent(player, packet.getChannel(), packet.getData())
        );
    }


    @Override
    public void handleChat(ClientChatPacket packet) {
        String message = packet.getMessage();
        if (message.startsWith("/")) {
            String command = message.substring(1);
            SagittariusImpl.getInstance().getCommandHandler().runCommand(player, command);
        }
    }

}
