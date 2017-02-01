package se.treehouse.minecraft.communication.message.data;

import com.google.common.collect.HashBiMap;
import org.bukkit.GameMode;

public class DataUtil {

    private DataUtil(){}

    private static final String GAME_MODE_CREATIVE = "CREATIVE";
    private static final String GAME_MODE_SURVIVAL = "SURVIVAL";
    private static final String GAME_MODE_ADVENTURE = "ADVENTURE";
    private static final String GAME_MODE_SPECTATOR = "SPECTATOR";

    private static final String STATE_OFF = "OFF";
    private static final String STATE_ON = "ON";

    private static HashBiMap<GameMode, String> GAME_MODE_MAP = HashBiMap.create();
    static {
        GAME_MODE_MAP.put(GameMode.CREATIVE, GAME_MODE_CREATIVE);
        GAME_MODE_MAP.put(GameMode.SURVIVAL, GAME_MODE_SURVIVAL);
        GAME_MODE_MAP.put(GameMode.ADVENTURE, GAME_MODE_ADVENTURE);
        GAME_MODE_MAP.put(GameMode.SPECTATOR, GAME_MODE_SPECTATOR);
    }

    private static HashBiMap<String, Boolean> SWITCH_MAP = HashBiMap.create();
    static {
        SWITCH_MAP.put(STATE_OFF, false);
        SWITCH_MAP.put(STATE_ON, true);
    }

    public static String gameModeToString(GameMode gameMode){
        return GAME_MODE_MAP.get(gameMode);
    }

    public static GameMode stringToGameMode(String gameMode){
        return GAME_MODE_MAP.inverse().get(gameMode.toUpperCase());
    }

    public static String switchStateToString(boolean state){
        String stringState = SWITCH_MAP.inverse().get(state);
        if(stringState != null){
            return stringState;
        }
        return STATE_OFF;
    }

    public static boolean stringToSwitchState(String stringState){
        Boolean state = SWITCH_MAP.get(stringState);
        if(state != null){
            return state;
        }
        return false;
    }
}
