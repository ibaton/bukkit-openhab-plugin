package se.treehouse.minecraft;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.bukkit.plugin.java.JavaPlugin;
import rx.Observable;
import se.treehouse.minecraft.communication.WSClientSocket;
import se.treehouse.minecraft.communication.discovery.DiscoveryService;
import se.treehouse.minecraft.communication.message.MessageUtil;
import se.treehouse.minecraft.communication.message.WSMessage;
import se.treehouse.minecraft.items.OHSign;
import spark.Spark;

import java.io.IOException;
import java.util.Collection;

public class WSMinecraft extends JavaPlugin {

    public static String PATH = "/stream";

    public static WSMinecraft plugin;
    private static final int DEFAULT_PORT = 10692;
    private DiscoveryService discoveryService;

    private BukkitServerListener serverListener;

    private Observable<WSMessage> messagesRx;

    private MessageUtil messageUtil;

    @Override
    public void onEnable() {
        messageUtil = new MessageUtil();
        plugin = this;
        saveDefaultConfig();
        int port = getConfig().getInt("port", DEFAULT_PORT);
        getLogger().info("Openhab plugin enabled");

        setupLog();

        serverListener = new BukkitServerListener();
        getServer().getPluginManager().registerEvents(serverListener, this);
        messagesRx = Observable.merge(
                serverListener.getServerRx().map(messageUtil::createServerMessage),
                serverListener.getPlayersRx().map(messageUtil::createPlayersMessage),
                serverListener.getSignsRx().map(messageUtil::createSignMessage));

        setupWebserver(port);
        setupDiscoveryService(port);
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
        getLogger().info("Openhab plugin setting upp server at port " + port);

        Spark.webSocket(PATH, WSClientSocket.class);
        Spark.init();
    }

    public Observable<Collection<OHSign>> getSignsRx(){
        return serverListener.getSignsRx();
    }

    public Collection<OHSign> getSigns(){
        return serverListener.getSigns();
    }

    public OHSign getSign(String signName){
        Collection<OHSign> signs = serverListener.getSigns();
        for(OHSign sign : signs){
            if(sign.getName().equalsIgnoreCase(signName)){
                return sign;
            }
        }

        return null;
    }

    private void setupLog(){
        Logger root = Logger.getRootLogger();
        root.setLevel(Level.INFO);
        try {
            DailyRollingFileAppender fa = new DailyRollingFileAppender(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss.SSSS} %p %t %c \u2013 %m%n"), "./logs/ohminecraft.log", "'.'yyyy-MM-dd");
            root.addAppender(fa);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Get observable emitting messages to send to clients.
     * @return observable emitting messages;
     */
    public Observable<WSMessage> getMessagesRx(){
        return messagesRx;
    }
}
