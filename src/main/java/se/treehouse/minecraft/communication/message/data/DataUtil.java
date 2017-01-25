package se.treehouse.minecraft.communication.message.data;

import com.google.common.collect.HashBiMap;
import org.bukkit.GameMode;

public class DataUtil {

    private DataUtil(){}

    private static final String GAME_MODE_CREATIVE = "CREATIVE";
    private static final String GAME_MODE_SURVIVAL = "SURVIVAL";
    private static final String GAME_MODE_ADVENTURE = "ADVENTURE";
    private static final String GAME_MODE_SPECTATOR = "SPECTATOR";

    private static HashBiMap<GameMode, String> GAME_MODE_MAP = HashBiMap.create();
    static {
        GAME_MODE_MAP.put(GameMode.CREATIVE, GAME_MODE_CREATIVE);
        GAME_MODE_MAP.put(GameMode.SURVIVAL, GAME_MODE_SURVIVAL);
        GAME_MODE_MAP.put(GameMode.ADVENTURE, GAME_MODE_ADVENTURE);
        GAME_MODE_MAP.put(GameMode.SPECTATOR, GAME_MODE_SPECTATOR);
    }

    public static String gameModeToString(GameMode gameMode){
        return GAME_MODE_MAP.get(gameMode);
    }

    public static GameMode stringToGameMode(String gameMode){
        return GAME_MODE_MAP.inverse().get(gameMode.toUpperCase());
    }
}
