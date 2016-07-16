package se.treehouse.minecraft.message.data;

import org.bukkit.Server;

/**
 * Data representing a minecraft server.
 * Can be sent in {@link se.treehouse.minecraft.message.WSMessage}
 */
public class ServerData {

    private String name;
    private String version;
    private String bukkitVersion;
    private int maxPlayers;
    private int players;

    /**
     * Creates a represtenetation of minecraft server.
     * @param server
     */
    public ServerData(Server server) {
        name = server.getName();
        bukkitVersion = server.getBukkitVersion();
        maxPlayers = server.getMaxPlayers();
        version = server.getVersion();
        players = server.getOnlinePlayers().size();
    }
}
