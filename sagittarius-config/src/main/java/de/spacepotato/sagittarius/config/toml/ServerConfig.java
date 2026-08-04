package de.spacepotato.sagittarius.config.toml;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration settings for the limbo server.
 *
 * @author a8kj7sea
 * @author viva_k1ng
 * @version 1.2.3
 */
@Getter
@Setter
public class ServerConfig {

    /**
     * The Message of the Day (MOTD) displayed in the Minecraft server list.
     */
    private String motd = "A Sagittarius Limbo Server";

    /**
     * The maximum number of players allowed to connect to the server concurrently.
     */
    private int maxPlayers = 1;
}