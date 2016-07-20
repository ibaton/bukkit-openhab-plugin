package se.treehouse.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.BasicConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import se.treehouse.minecraft.message.data.PlayerData;
import se.treehouse.minecraft.message.data.ServerData;
import se.treehouse.minecraft.message.WSMessage;
import se.treehouse.minecraft.items.OHSign;
import spark.Spark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WSMinecraft extends JavaPlugin {

    public static String PATH = "/stream";

    private Gson gson = new GsonBuilder().create();
    public static WSMinecraft plugin;
    public static final int DEFAULT_PORT = 10692;
    public Collection<OHSign> signs = new ArrayList<>();
    private DiscoveryService discoveryService;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        int port = getConfig().getInt("port", DEFAULT_PORT);
        getLogger().info("Openhab plugin enabled");

        setupWebserver(port);
        setupDiscoveryService(port);

        getServer().getPluginManager().registerEvents(serverListener, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Openhab plugin disabled");
        Spark.stop();
        discoveryService.deregisterService();
    }

    public static WSMinecraft instance() {
        return plugin;
    }

    /**
     * Setup minecraft webserver.
     *
     * @param port the port to setup server on.
     */
    private void setupWebserver(int port){
        Spark.port(port);
        getLogger().info("Openhab plugin setting upp server att port " + port);
        BasicConfigurator.configure();

        Spark.webSocket(PATH, WSClientSocket.class);
        Spark.init();
    }

    /**
     * Setup discovery service for plugin.
     *
     * @param port the port to broadcast.
     */
    private void setupDiscoveryService(int port){
        discoveryService = new DiscoveryService();
        discoveryService.registerService(port);
    }

    /**
     * Gather player data and package it in a a socket message.
     * @return message with player data
     */
    public WSMessage createPlayersMessage(){
        List<PlayerData> playerDatas = Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
        return new WSMessage(WSMessage.MESSAGE_TYPE_PLAYERS, gson.toJsonTree(playerDatas));
    }

    /**
     * Gather server data and package it in a socket message
     * @return message with socket data
     */
    public WSMessage createServerMessage(){
        ServerData serverData = new ServerData(Bukkit.getServer());
        return new WSMessage(WSMessage.MESSAGE_TYPE_SERVERS, gson.toJsonTree(serverData));
    }

    /**
     * Package signs data into a socket message.
     * @param signs signs to send.
     * @return message containing sign data
     */
    public WSMessage createSignMessage(Collection<OHSign> signs){
        return new WSMessage(WSMessage.MESSAGE_TYPE_SIGNS, gson.toJsonTree(signs));
    }

    BukkitServerListener serverListener = new BukkitServerListener(new BukkitServerListener.ServerListener() {
        @Override
        public void onPlayersUpdate(Collection<? extends Player> players) {
            WSClientSocket.broadcastMessage(createPlayersMessage());
        }

        @Override
        public void onSignsUpdate(Collection<OHSign> signs) {
            WSMinecraft.this.signs = signs;
            WSClientSocket.broadcastMessage(createSignMessage(signs));
        }

        @Override
        public void onServerUpdate(Server server) {
            WSClientSocket.broadcastMessage(createServerMessage());
        }
    });
}
