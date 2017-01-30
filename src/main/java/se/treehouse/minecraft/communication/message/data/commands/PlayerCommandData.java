package se.treehouse.minecraft.communication.message.data.commands;

public class PlayerCommandData {

    public static String COMMAND_PLAYER_HEALTH = "PLAYER_HEALTH";
    public static String COMMAND_PLAYER_LEVEL = "PLAYER_LEVEL";
    public static String COMMAND_PLAYER_WALK_SPEED = "PLAYER_WALK_SPEED";
    public static String COMMAND_PLAYER_GAME_MODE = "PLAYER_GAME_MODE";
    public static String COMMAND_PLAYER_LOCATION = "PLAYER_LOCATION";

    String type;
    String playerName;
    String value;

    public PlayerCommandData() {
    }

    public PlayerCommandData(String type, String playerName, String value) {
        this.type = type;
        this.playerName = playerName;
        this.value = value;
    }

    /**
     * Get the type of command.
     *
     * @return the type of command.
     */
    public String getType() {
        return type;
    }

    /**
     * The name of the player that the command targets.
     *
     * @return name of player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * The command value sent.
     *
     * @return command value.
     */
    public String getValue() {
        return value;
    }
}
