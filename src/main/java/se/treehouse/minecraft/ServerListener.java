package se.treehouse.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public final class ServerListener implements Listener {

    PlayerListener playerListener = null;

    public ServerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        updatePlayers();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEvent(PlayerEvent event) {
        updatePlayers();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent event) {
        updatePlayers();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        updatePlayers();
    }

    private void updatePlayers(){
        playerListener.onPlayersUpdate(Bukkit.getOnlinePlayers());
    }

    public interface PlayerListener {
        void onPlayersUpdate(Collection<? extends Player> players);
    }
}
