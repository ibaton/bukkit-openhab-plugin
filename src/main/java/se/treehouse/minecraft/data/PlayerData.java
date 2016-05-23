package se.treehouse.minecraft.data;

import org.bukkit.entity.Player;

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
