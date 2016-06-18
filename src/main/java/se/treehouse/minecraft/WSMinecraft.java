package se.treehouse.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.BasicConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import se.treehouse.minecraft.data.PlayerData;
import se.treehouse.minecraft.data.ServerData;
import se.treehouse.minecraft.message.WSMessage;
import spark.Spark;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WSMinecraft extends JavaPlugin {

    private Gson gson;
    public static WSMinecraft plugin;
    public static final int DEFAULT_PORT = 10692;
    private DiscoveryService discoveryService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        plugin = this;

        gson = new GsonBuilder().create();

        getLogger().info("Openhab plugin enabled");

        FileConfigurationOptions fileConfigurationOptions = getConfig().options().copyDefaults(true);

        int port = getConfig().getInt("port", DEFAULT_PORT);

        Spark.port(port);
        getLogger().info("Openhab plugin setting upp server att port " + port);
        BasicConfigurator.configure();

        Spark.webSocket("/stream", WSClientSocket.class);
        Spark.init();
        discoveryService = new DiscoveryService();
        discoveryService.start(port);

        getServer().getPluginManager().registerEvents(serverListener, this);
    }

    public static WSMinecraft instance() {
        return plugin;
    }

    public WSMessage createPlayersMessage(){
        List<PlayerData> playerDatas = Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
        return new WSMessage(WSMessage.MESSAGE_TYPE_PLAYERS, gson.toJsonTree(playerDatas));
    }

    public WSMessage createServerMessage(){
        ServerData serverData = new ServerData(Bukkit.getServer());
        return new WSMessage(WSMessage.MESSAGE_TYPE_SERVERS, gson.toJsonTree(serverData));
    }

    public WSMessage createSignMessage(Collection<BukkitServerListener.OHSign> signs){
        return new WSMessage(WSMessage.MESSAGE_TYPE_SIGNS, gson.toJsonTree(signs));
    }

    public List<PlayerData> getPlayers(){
        return Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
    }

    @Override
    public void onDisable() {
        getLogger().info("Openhab plugin disabled");
        Spark.stop();
        discoveryService.stop();
    }

    BukkitServerListener serverListener = new BukkitServerListener(new BukkitServerListener.ServerListener() {
        @Override
        public void onPlayersUpdate(Collection<? extends Player> players) {
            WSClientSocket.broadcastMessage(createPlayersMessage());
        }

        @Override
        public void onSignsUpdate(Collection<BukkitServerListener.OHSign> signs) {
            WSClientSocket.broadcastMessage(createSignMessage(signs));
        }

        @Override
        public void onServerUpdate(Server server) {
            WSClientSocket.broadcastMessage(createServerMessage());
        }
    });
}
