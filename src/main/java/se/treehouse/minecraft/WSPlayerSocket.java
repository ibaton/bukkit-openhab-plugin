package se.treehouse.minecraft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import se.treehouse.minecraft.message.WSMessage;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebSocket
public class WSPlayerSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private static Gson gson = new GsonBuilder().create();

    @OnWebSocketConnect
    public void connected(Session session) {
        WSMinecraft.plugin.getLogger().info("Connected");
        sessions.add(session);
        broadcastMessage(WSMinecraft.instance().createServerMessage());
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        WSMinecraft.plugin.getLogger().info("Closed");
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        WSMinecraft.plugin.getLogger().info("Got: " + message);

        WSMessage ohMessage = gson.fromJson(message, WSMessage.class);
        ohMessage.getMessageType();

        if(WSMessage.MESSAGE_TYPE_PLAYERS == ohMessage.getMessageType()){
            broadcastMessage(WSMinecraft.instance().createPlayersMessage());
        }
    }

    public static void broadcastMessage(WSMessage message){

        String jsonMessage = gson.toJson(message);
        sessions.stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
