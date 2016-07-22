package se.treehouse.minecraft.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import rx.Subscription;
import se.treehouse.minecraft.WSMinecraft;
import se.treehouse.minecraft.communication.message.WSMessage;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Handles communication with clients.
 */
@WebSocket
public class WSClientSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private static Gson gson = new GsonBuilder().create();
    private Subscription subscription;

    public WSClientSocket() {}

    /**
     * Client connected to server.
     * @param session the client session created.
     */
    @OnWebSocketConnect
    public void connected(Session session) {
        WSMinecraft.plugin.getLogger().info("Connected");
        sessions.add(session);

        subscription = WSMinecraft.instance().getMessagesRx().subscribe(WSClientSocket::broadcastMessage);
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

        WSMinecraft.plugin.getLogger().info("Closed");
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
    }

    /**
     * Sends message to all connected clients.
     * @param message the message that was sent.
     */
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
