package commands;

public enum Types {
    TYPE,
    CD,
    PWD,
    ECHO,
    EXIT,
    UNKNOWN;

    public static Types getType(String type) {
        for (Types t : values()) {
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
