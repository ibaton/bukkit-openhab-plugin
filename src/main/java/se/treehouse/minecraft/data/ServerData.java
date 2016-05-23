package se.treehouse.minecraft.data;

import org.bukkit.Server;

public class ServerData {

    String name;
    String version;
    String bukkitVersion;
    int maxPlayers;
    int players;

    public ServerData(Server server) {
        name = server.getName();
        bukkitVersion = server.getBukkitVersion();
        maxPlayers = server.getMaxPlayers();
        version = server.getVersion();
        players = server.getOnlinePlayers().size();
    }
}
