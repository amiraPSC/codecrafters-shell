package commands;

public enum CommandsTypes {
    TYPE,
    CD,
    PWD,
    ECHO,
    COMPLETE,
    EXIT,
    UNKNOWN;

    public static CommandsTypes getType(String type) {
        for (CommandsTypes t : values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        return UNKNOWN;
    }

    public static boolean isBuiltin(String type) {
        return getType(type) != UNKNOWN;
    }
}
