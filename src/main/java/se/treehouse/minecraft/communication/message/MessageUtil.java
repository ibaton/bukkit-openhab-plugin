package se.treehouse.minecraft.communication.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import se.treehouse.minecraft.communication.message.data.PlayerData;
import se.treehouse.minecraft.communication.message.data.ServerData;
import se.treehouse.minecraft.items.OHSign;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MessageUtil {

    private static Gson gson = new GsonBuilder().create();

    /**
     * Gather player data and package it in a a socket message.
     * @return message with player data
     */
    public WSMessage createPlayersMessage(Collection<? extends Player> players){
        List<PlayerData> playerDatas = players.stream().map(PlayerData::new).collect(Collectors.toList());
        return new WSMessage(WSMessage.MESSAGE_TYPE_PLAYERS, gson.toJsonTree(playerDatas));
    }

    /**
     * Gather server data and package it in a socket message
     * @return message with socket data
     */
    public WSMessage createServerMessage(Server server){
        ServerData serverData = new ServerData(server);
        return new WSMessage(WSMessage.MESSAGE_TYPE_SERVERS, gson.toJsonTree(serverData));
    }

    /**
     * Package signs data into a socket message.
     * @param signs signs to send.
     * @return message containing sign data
     */
    public WSMessage createSignMessage(Collection<OHSign> signs){
        return new WSMessage(WSMessage.MESSAGE_TYPE_SIGNS, gson.toJsonTree(signs));
    }
}
