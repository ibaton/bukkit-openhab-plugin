package se.treehouse.minecraft.items;

import se.treehouse.minecraft.communication.message.data.LocationData;

/**
 * A sign on minecraft server.
 */
public class OHSign {

    private String name;
    private boolean state = false;
    private LocationData location;

    /**
     * Creates a sign holds information about its name (text printed on it)
     * and if block under it is powered by Redstone.
     *
     * @param name the text on sign.
     * @param state if block under it is powered by redstone.
     * @param location location of sign.
     */
    public OHSign(String name, boolean state, LocationData location) {
        this.name = name;
        this.state = state;
        this.location = location;
    }

    /**
     * The text on sign.
     * @return the text on sign.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the state of sign.
     * @param state
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Get powered state of sign.
     * @return true if sign is powered by redstone.
     */
    public boolean getState(){
        return state;
    }

    /**
     * Get location of sign.
     *
     * @return location of sign.
     */
    public LocationData getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OHSign ohSign = (OHSign) o;

        if (state != ohSign.state) return false;
        if (name != null ? !name.equals(ohSign.name) : ohSign.name != null) return false;
        return location != null ? location.equals(ohSign.location) : ohSign.location == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (state ? 1 : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
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
