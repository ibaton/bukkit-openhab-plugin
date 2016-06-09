package se.treehouse.minecraft;

import com.avaje.ebeaninternal.server.lib.sql.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import se.treehouse.minecraft.data.LocationData;

import java.util.*;
import java.util.concurrent.*;

public final class ServerListener implements Listener {

    private PlayerListener playerListener = null;
    private ScheduledThreadPoolExecutor excecutor = new ScheduledThreadPoolExecutor(1);

    private Map<LocationData, OHSign> ohSigns = new HashMap<>();

    public ServerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockDestroyEvent(BlockBreakEvent event) {

        WSMinecraft.plugin.getLogger().info("Block Destroyed: " + new LocationData(event.getBlock().getLocation()));

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

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockRedstoneEvent(BlockRedstoneEvent event) {
        Location topPosition = event.getBlock().getLocation().add(0, 1, 0);
        LocationData topPositionData = new LocationData(topPosition);
        OHSign sign = ohSigns.get(topPositionData);
        if(sign == null){
            Block topBlock = topPosition.getBlock();
            if(topBlock instanceof Sign){
                Sign signBlock = (Sign) topBlock;
                sign = new OHSign(signBlock.getLines()[0], false, topPositionData);
                ohSigns.put(topPositionData, sign);
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();

        LocationData location = new LocationData(event.getBlock().getLocation());
        OHSign ohSign = new OHSign(lines[0], false, location);

        WSMinecraft.plugin.getLogger().info("Added sign: " + ohSign);

        ohSigns.put(ohSign.getLocation(), ohSign);

        broadcastSigns();
    }

    private void broadcastSigns(){
        updateSigns(ohSigns.values());
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

    private void updateSigns(Collection<OHSign> signs){
        playerListener.onSignsUpdate(signs);
    }

    private void updateServer(Server server){
        playerListener.onServerUpdate(server);
    }

    public interface PlayerListener {
        void onPlayersUpdate(Collection<? extends Player> players);
        void onServerUpdate(Server server);
        void onSignsUpdate(Collection<OHSign> signs);
    }

    public static class OHSign {

        String name;
        boolean state = false;
        LocationData location;

        public OHSign(String name, boolean state, LocationData location) {
            this.name = name;
            this.state = state;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public boolean getState(){
            return state;
        }

        public LocationData getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "OHSign{" +
                    "name='" + name + "'" +
                    ", state=" + state +
                    ", location=" + location +
                    '}';
        }
    }
}
