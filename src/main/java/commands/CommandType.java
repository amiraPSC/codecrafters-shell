package commands;

public enum CommandType {
    TYPE,
    CD,
    PWD,
    ECHO,
    JOBS,
    COMPLETE,
    EXIT,
    UNKNOWN;

    public static CommandType getType(String type) {
        for (CommandType t : values()) {
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
