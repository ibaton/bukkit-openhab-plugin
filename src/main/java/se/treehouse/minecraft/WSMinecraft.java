package se.treehouse.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.BasicConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import se.treehouse.minecraft.data.PlayerData;
import se.treehouse.minecraft.data.ServerData;
import se.treehouse.minecraft.message.WSMessage;
import spark.Spark;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WSMinecraft extends JavaPlugin {

    private Gson gson;
    public static WSMinecraft plugin;
    private DiscoveryService discoveryService;

    @Override
    public void onEnable() {
        plugin = this;

        gson = new GsonBuilder().create();

        getLogger().info("Openhab plugin enabled");

        Spark.port(10692);
        BasicConfigurator.configure();

        Spark.webSocket("/stream", WSPlayerSocket.class);
        Spark.init();
        discoveryService = new DiscoveryService();
        discoveryService.start();

        getServer().getPluginManager().registerEvents(playerListener, this);
    }

    public static class DiscoveryService{

        private JmDNS jmdns;

        public void start() {
            try {
                jmdns = JmDNS.create();
                jmdns.registerService(
                        ServiceInfo.create("_http._tcp.local.", "wc-minecraft", 10692, "")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            if(jmdns != null) {
                try {
                    jmdns.unregisterAllServices();
                    jmdns.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public List<PlayerData> getPlayers(){
        return Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
    }

    @Override
    public void onDisable() {
        getLogger().info("Openhab plugin disabled");
        Spark.stop();
        discoveryService.stop();
    }

    ServerListener playerListener = new ServerListener(new ServerListener.PlayerListener() {
        @Override
        public void onPlayersUpdate(Collection<? extends Player> players) {
            WSPlayerSocket.broadcastMessage(createPlayersMessage());
        }

        @Override
        public void onServerUpdate(Server server) {
            WSPlayerSocket.broadcastMessage(createServerMessage());
        }
    });
}
