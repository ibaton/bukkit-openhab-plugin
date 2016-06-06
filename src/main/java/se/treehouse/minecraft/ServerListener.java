package se.treehouse.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Collection;
import java.util.concurrent.*;

public final class ServerListener implements Listener {

    private PlayerListener playerListener = null;
    private ScheduledThreadPoolExecutor excecutor = new ScheduledThreadPoolExecutor(1);

    public ServerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Server server = Bukkit.getServer();
        updatePlayers();
        updateServer(server);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent event) {
        updatePlayers();
        excecutor.schedule((Runnable) () -> updateServer(Bukkit.getServer()), 200, TimeUnit.MILLISECONDS);
    }

    private void updatePlayers(){
        playerListener.onPlayersUpdate(Bukkit.getOnlinePlayers());
    }

    private void updateServer(Server server){
        playerListener.onServerUpdate(server);
    }

    public interface PlayerListener {
        void onPlayersUpdate(Collection<? extends Player> players);
        void onServerUpdate(Server server);
    }
}
