package se.treehouse.minecraft.communication.message;

import com.google.gson.JsonElement;

public class WSMessage {

    public static final int MESSAGE_TYPE_PLAYERS = 1;
    public static final int MESSAGE_TYPE_SERVERS = 2;
    public static final int MESSAGE_TYPE_SIGNS = 4;
    public static final int MESSAGE_TYPE_PLAYER_COMMANDS = 3;
    public static final int MESSAGE_TYPE_SIGN_COMMANDS = 5;

    int messageType;
    JsonElement message;

    public WSMessage(int messageType, JsonElement message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    /**
     * Set the type of message sent.
     * @param messageType the type of message sent.
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * Get the data attached to message
     * @return the data sent in message.
     */
    public JsonElement getMessage() {
        return message;
    }

    /**
     * The data to send to listening clients.
     * @param message the message to send.
     */
    public void setMessage(JsonElement message) {
        this.message = message;
    }
}
