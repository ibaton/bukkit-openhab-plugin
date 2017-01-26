package se.treehouse.minecraft.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import rx.Subscription;
import rx.functions.Action1;
import se.treehouse.minecraft.WSMinecraft;
import se.treehouse.minecraft.communication.message.data.DataUtil;
import se.treehouse.minecraft.communication.message.WSMessage;
import se.treehouse.minecraft.communication.message.data.commands.PlayerCommandData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static se.treehouse.minecraft.communication.message.WSMessage.MESSAGE_TYPE_PLAYER_COMMANDS;
import static se.treehouse.minecraft.communication.message.data.commands.PlayerCommandData.*;

/**
 * Handles communication with clients.
 */
@WebSocket
public class WSClientSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private static Gson gson = new GsonBuilder().create();
    private Subscription subscription;

    private Map<String, Action1<PlayerCommandData>> commandMap = new HashMap<>();
    {
        commandMap.put(COMMAND_PLAYER_HEALTH, this::handlePlayerHealthCommand);
        commandMap.put(COMMAND_PLAYER_LEVEL, this::handlePlayerLevelCommand);
        commandMap.put(COMMAND_PLAYER_WALK_SPEED, this::handlePlayerWalkSpeedCommand);
        commandMap.put(COMMAND_PLAYER_GAME_MODE, this::handlePlayerGameModeCommand);
        commandMap.put(COMMAND_PLAYER_LOCATION, this::handlePlayerLocationCommand);
    }

    public WSClientSocket() {}

    /**
     * Client connected to server.
     * @param session the client session created.
     */
    @OnWebSocketConnect
    public void connected(Session session) {
        WSMinecraft.plugin.getLogger().info("Connection from " + session.getRemoteAddress());
        sessions.add(session);

        subscription = WSMinecraft.instance().getMessagesRx().subscribe(message -> SendMessage(session, message));
        session.getRemote();
    }

    /**
     * Session with client closed
     *
     * @param session the session that was closed.
     * @param statusCode the status code that session was closed with.
     * @param reason the reson that te connection was closed.
     */
    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);

        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        WSMinecraft.plugin.getLogger().info("Closed connection from " + session.getRemoteAddress());
    }

    /**
     * Message received from client.
     *
     * @param session the session that the message was received from
     * @param message the message data that was sent from client.
     * @throws IOException
     */
    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        try {
            WSMessage wsMessage = gson.fromJson(message, WSMessage.class);
            int messageType = wsMessage.getMessageType();

            if(MESSAGE_TYPE_PLAYER_COMMANDS == messageType){
                PlayerCommandData commandData = gson.fromJson(wsMessage.getMessage(), PlayerCommandData.class);

                WSMinecraft.instance().getLogger().info(String.format("Player command: %s %s %s",
                        commandData.getPlayerName(), commandData.getType() ,commandData.getCommand()));

                if(commandMap.containsKey(commandData.getType())){
                    commandMap.get(commandData.getType()).call(commandData);
                }
            }
        } catch (Exception e){}
    }

    private void handlePlayerHealthCommand(PlayerCommandData commandData){
        double playerHealth = Double.valueOf(commandData.getCommand());
        String playerName = commandData.getPlayerName();

        WSMinecraft.instance().getLogger().info(String.format("Setting %s health: %.1f", playerName , playerHealth));
        getPlayer(playerName).setHealth(playerHealth);
    }

    private void handlePlayerLevelCommand(PlayerCommandData commandData){
        int level = Integer.valueOf(commandData.getCommand());
        String playerName = commandData.getPlayerName();

        WSMinecraft.instance().getLogger().info(String.format("Setting %s level: %d", playerName, level));
        getPlayer(playerName).setLevel(level);
    }

    private void handlePlayerGameModeCommand(PlayerCommandData commandData){
        String gameModeName = commandData.getCommand();
        String playerName = commandData.getPlayerName();

        GameMode gameMode = DataUtil.stringToGameMode(gameModeName);
        if(gameMode != null) {
            WSMinecraft.instance().getLogger().info(String.format("Setting %s gamemode: %s", playerName, gameMode));
            getPlayer(playerName).setGameMode(gameMode);
        } else {
            WSMinecraft.instance().getLogger().warning(String.format("Failed to get gamemode: %s", gameModeName));
        }
    }

    private void handlePlayerWalkSpeedCommand(PlayerCommandData commandData){
        float walkSpeed = Float.valueOf(commandData.getCommand());
        walkSpeed = Math.max(0, walkSpeed);
        walkSpeed = Math.min(1, walkSpeed);
        String playerName = commandData.getPlayerName();

        WSMinecraft.instance().getLogger().info(String.format("Setting %s walk speed: %f", playerName, walkSpeed));
        getPlayer(playerName).setWalkSpeed(walkSpeed);
    }

    private void handlePlayerLocationCommand(PlayerCommandData commandData){
        String[] locationData = commandData.getCommand().split(",");
        double locationX = Double.valueOf(locationData[0]);
        double locationY = Double.valueOf(locationData[1]);
        double locationZ = Double.valueOf(locationData[2]);
        String playerName = commandData.getPlayerName();

        WSMinecraft.instance().getLogger().info(String.format("Setting %s location: x:%.1f y:%.1f z:%.1f",
                playerName, locationX, locationY, locationZ));
        Player player = getPlayer(playerName);
        player.teleport(new Location(player.getWorld(), locationX, locationY, locationZ));
    }


    /**
     * Get player from name-
     * @param playerName the player to get
     * @return player object for provided player name.
     */
    private Player getPlayer(String playerName){
        return WSMinecraft.instance().getServer().getPlayer(playerName);
    }

    /**
     * Sends message to all connected clients.
     * @param message the message that was sent.
     */
    public static void SendMessage(Session session, WSMessage message){
        String jsonMessage = gson.toJson(message);
        if(session.isOpen()) {
            try {
                session.getRemote().sendString(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
