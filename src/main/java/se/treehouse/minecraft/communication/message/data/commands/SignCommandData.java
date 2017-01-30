package se.treehouse.minecraft.communication.message.data.commands;

public class SignCommandData {

    public static String COMMAND_SIGN_ACTIVE = "COMMAND_SIGN_ACTIVE";

    String type;
    String signName;
    String value;

    public SignCommandData() {
    }

    public SignCommandData(String type, String signName, String value) {
        this.type = type;
        this.signName = signName;
        this.value = value;
    }

    /**
     * Get the type of command.
     *
     * @return the type of command.
     */
    public String getType() {
        return type;
    }

    /**
     * The name of the sign that the command targets.
     *
     * @return name of sign
     */
    public String getSignName() {
        return signName;
    }

    /**
     * The command value sent.
     *
     * @return command value.
     */
    public String getValue() {
        return value;
    }
}
