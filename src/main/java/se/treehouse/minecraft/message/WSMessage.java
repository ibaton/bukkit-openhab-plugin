package se.treehouse.minecraft.message;

import com.google.gson.JsonElement;

public class WSMessage {

    public static final int MESSAGE_TYPE_PLAYERS = 1;
    public static final int MESSAGE_TYPE_SERVERS = 2;
    public static final int MESSAGE_TYPE_COMMANDS = 3;
    public static final int MESSAGE_TYPE_SIGNS = 4;

    int messageType;
    JsonElement message;

    public WSMessage(int messageType, JsonElement message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public JsonElement getMessage() {
        return message;
    }

    public void setMessage(JsonElement message) {
        this.message = message;
    }
}
