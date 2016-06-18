package se.treehouse.minecraft.data;

import org.bukkit.Location;

/**
 * Data representing a location in minecraft world.
 * Can be sent in {@link se.treehouse.minecraft.message.WSMessage}
 */
public class LocationData {
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    /**
     * Crates a location object.
     * @param location data as it is represented by spigot server.
     */
    public LocationData(Location location) {
        x = location.getX();
        y = location.getY();
        z = location.getZ();
        pitch = location.getPitch();
        yaw = location.getYaw();
    }

    /**
     * The x coordinate of location.
     *
     * @return x coordinate of location.
     */
    public double getX() {
        return x;
    }

    /**
     * Set the x coordinate of location.
     * @param x the x location.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * The y coordinate of location.
     *
     * @return y coordinate of location.
     */
    public double getY() {
        return y;
    }

    /**
     * Set the y coordinate of location.
     * @param y the y location.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * The z coordinate of location.
     *
     * @return z coordinate of location.
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the z coordinate of location.
     * @param z the z location.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Get the pitch of object in location.
     * @return the pitch of object in location.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Set the pitch location of object in location.
     * @param pitch th pitch of object in location.
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Get the yaw of object in location.
     * @return the yaw of object in location.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * set the yaw of location.
     * @param yaw the yaw of object in location.
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationData that = (LocationData) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        return Double.compare(that.z, z) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "z=" + z +
                ", y=" + y +
                ", x=" + x +
                '}';
    }
}
