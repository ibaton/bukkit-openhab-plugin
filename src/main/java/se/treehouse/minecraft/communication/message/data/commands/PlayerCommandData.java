package se.treehouse.minecraft.communication.message.data.commands;

public class PlayerCommandData {

    public static String COMMAND_PLAYER_HEALTH = "PLAYER_HEALTH";
    public static String COMMAND_PLAYER_LEVEL = "PLAYER_LEVEL";
    public static String COMMAND_PLAYER_WALK_SPEED = "PLAYER_WALK_SPEED";
    public static String COMMAND_PLAYER_GAME_MODE = "PLAYER_GAME_MODE";
    public static String COMMAND_PLAYER_LOCATION = "PLAYER_LOCATION";

    String type;
    String playerName;
    String command;

    public PlayerCommandData() {
    }

    public PlayerCommandData(String type, String playerName, String command) {
        this.type = type;
        this.playerName = playerName;
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCommand() {
        return command;
    }
}
