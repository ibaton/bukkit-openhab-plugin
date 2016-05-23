package se.treehouse.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.BasicConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import se.treehouse.minecraft.data.PlayerData;
import se.treehouse.minecraft.data.ServerData;
import se.treehouse.minecraft.message.OHMessage;
import spark.Spark;

import java.util.List;
import java.util.stream.Collectors;

public class OHMinecraft extends JavaPlugin {

    Gson gson;
    public static OHMinecraft plugin;

    @Override
    public void onEnable() {
        plugin = this;

        gson = new GsonBuilder().create();

        getLogger().info("Openhab plugin enabled");

        Spark.port(10692);
        BasicConfigurator.configure();

        Spark.webSocket("/stream", OHPlayerSocket.class);
        Spark.init();

        getServer().getPluginManager().registerEvents(playerListener, this);
    }

    public static OHMinecraft instance() {
        return plugin;
    }

    public OHMessage createPlayersMessage(){
        List<PlayerData> playerDatas = Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
        return new OHMessage(OHMessage.MESSAGE_TYPE_PLAYERS, gson.toJsonTree(playerDatas));
    }

    public OHMessage createServerMessage(){
        ServerData serverData = new ServerData(Bukkit.getServer());
        return new OHMessage(OHMessage.MESSAGE_TYPE_SERVERS, gson.toJsonTree(serverData));
    }

    public List<PlayerData> getPlayers(){
        return Bukkit.getOnlinePlayers().stream().map(PlayerData::new).collect(Collectors.toList());
    }

    @Override
    public void onDisable() {
        getLogger().info("Openhab plugin disabled");
        Spark.stop();
    }

    ServerListener playerListener = new ServerListener(players -> {
        OHPlayerSocket.broadcastMessage(createServerMessage());
        OHPlayerSocket.broadcastMessage(createPlayersMessage());
    });
}
