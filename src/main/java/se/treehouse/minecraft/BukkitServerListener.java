package se.treehouse.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import se.treehouse.minecraft.message.data.LocationData;
import se.treehouse.minecraft.items.OHSign;

import java.util.*;
import java.util.concurrent.*;

/**
 * Listens for changes on minecraft server.
 */
public final class BukkitServerListener implements Listener {

    private ServerListener serverListener = null;
    private ScheduledThreadPoolExecutor excecutor = new ScheduledThreadPoolExecutor(1);
    private Map<LocationData, OHSign> ohSigns = new HashMap<>();

    /**
     * Listens for changes of minecraft server.
     *
     * @param serverListener listens for minecraft servers.
     */
    public BukkitServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    /**
     * Listen for block breaking.
     * Updates clients if redsone or or signs break.
     *
     * @param event block destroy events
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockDestroyEvent(BlockBreakEvent event) {

        OHSign removedSign = ohSigns.remove(new LocationData(event.getBlock().getLocation()));
        if(removedSign != null){
            broadcastSigns();
        }else {
            OHSign sign = ohSigns.get(new LocationData(event.getBlock().getLocation().add(0,1,0)));
            if(sign != null){
                sign.setState(false);
                updateSigns(ohSigns.values());
                broadcastSigns();
            }
        }
    }

    /**
     * Listen for redstone changes.
     * Updates sign state to reflect changes.
     *
     * @param event the redstone event.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockRedstoneEvent(BlockRedstoneEvent event) {
        Location topPosition = event.getBlock().getLocation().add(0, 1, 0);
        LocationData topPositionData = new LocationData(topPosition);
        OHSign sign = ohSigns.get(topPositionData);
        if(sign == null){
            BlockState topBlock = topPosition.getBlock().getState();
            if(topBlock instanceof Sign){
                Sign signBlock = (Sign) topBlock;
                sign = new OHSign(signBlock.getLines()[0], false, topPositionData);
                ohSigns.put(topPositionData, sign);
                WSMinecraft.plugin.getLogger().info("Found new sign " + sign.getName());
            }
        }

        if(sign != null){
            boolean newState = event.getNewCurrent() > 0;
            if(sign.getState() != newState) {
                sign.setState(event.getNewCurrent() > 0);
                updateSigns(ohSigns.values());
                WSMinecraft.plugin.getLogger().info("Added sign: " + sign);

                broadcastSigns();
            }
        }
    }

    /**
     * Listen for changes in sign state.
     * Notifies clients that a new sign is created.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();

        LocationData location = new LocationData(event.getBlock().getLocation());
        OHSign ohSign = new OHSign(lines[0], false, location);

        WSMinecraft.plugin.getLogger().info("Added sign: " + ohSign);
        ohSigns.put(ohSign.getLocation(), ohSign);

        broadcastSigns();
    }

    /**
     * Sends out state of all signs to all listening clients.
     */
    private void broadcastSigns(){
        updateSigns(ohSigns.values());
    }

    /**
     * Listen for login event for player.
     * Notifies connected client that player is online.
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Server server = Bukkit.getServer();
        updatePlayers();
        updateServer(server);
    }

    /**
     * Listen for logout event for player.
     * Notifies connected client that player is offline.
     *
     * @param event logout event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent event) {
        updatePlayers();
        excecutor.schedule((Runnable) () -> updateServer(Bukkit.getServer()), 200, TimeUnit.MILLISECONDS);
    }

    private void updatePlayers(){
        serverListener.onPlayersUpdate(Bukkit.getOnlinePlayers());
    }

    /**
     * Notify listener that sign configuration has been updated.
     * @param signs the state of signs.
     */
    private void updateSigns(Collection<OHSign> signs){
        serverListener.onSignsUpdate(signs);
    }

    /** {@inheritDoc} */
    private void updateServer(Server server){
        serverListener.onServerUpdate(server);
    }

    public interface ServerListener {
        void onPlayersUpdate(Collection<? extends Player> players);

        /**
         * Notify listener that server configuration has been updated.
         * @param server the state of server.
         */
        void onServerUpdate(Server server);

        /**
         * Notify listener that sign configuration has been updated.
         * @param signs the state of signs.
         */
        void onSignsUpdate(Collection<OHSign> signs);
    }
}
