package se.treehouse.minecraft.communication.message.data;

import org.bukkit.entity.Player;

/**
 * Data representing a player on Minecraft server.
 * Can be sent in {@link se.treehouse.minecraft.communication.message.WSMessage}
 */
public class PlayerData {

    protected String displayName;
    protected String name;
    protected int level;
    protected int totalExperience;
    protected float experience;
    protected double health;
    protected double healthScale;
    protected float walkSpeed;
    protected LocationData location;

    /**
     * Creates a representation of player.
     * @param player the data representing a player.
     */
    public PlayerData(Player player) {
        name = player.getName();
        displayName = player.getDisplayName();
        totalExperience = player.getTotalExperience();
        experience = player.getExp();
        health = player.getHealth();
        healthScale = player.getHealthScale();
        level = player.getLevel();
        walkSpeed = player.getWalkSpeed();
        location = new LocationData(player.getLocation());
    }
}
