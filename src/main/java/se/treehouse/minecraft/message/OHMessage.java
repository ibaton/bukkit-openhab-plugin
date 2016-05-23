package se.treehouse.minecraft.message;

import com.google.gson.JsonElement;

public class OHMessage {

    public static final int MESSAGE_TYPE_PLAYERS = 1;
    public static final int MESSAGE_TYPE_SERVERS = 2;
    public static final int MESSAGE_TYPE_COMMANDS = 3;

    int messageType;
    JsonElement message;

    public OHMessage(int messageType, JsonElement message) {
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
