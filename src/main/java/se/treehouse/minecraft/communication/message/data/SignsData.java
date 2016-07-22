package se.treehouse.minecraft.communication.message.data;

/**
 * Data representing a sign on minecraft server.
 * Can be sent in {@link se.treehouse.minecraft.communication.message.WSMessage}
 */
public class SignsData {

    private String name;
    private boolean state;

    /**
     * Creates a object representing a sign on server.
     *
     * @param name text printed on sign.
     * @param state if sign is powered by redstone.
     */
    public SignsData(String name, boolean state) {
        this.name = name;
        this.state = state;
    }
}
