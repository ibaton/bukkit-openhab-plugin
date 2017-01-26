package se.treehouse.minecraft.communication.message.data.commands;

public class SignCommandData {

    public static String COMMAND_SIGN_ACTIVE = "COMMAND_SIGN_ACTIVE";

    String type;
    String signName;
    String command;

    public SignCommandData() {
    }

    public SignCommandData(String type, String signName, String command) {
        this.type = type;
        this.signName = signName;
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public String getSignName() {
        return signName;
    }

    public String getCommand() {
        return command;
    }
}
