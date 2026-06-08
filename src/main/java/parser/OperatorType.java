package parser;

public enum OperatorType {
    STDOUT_REDIRECT,
    STDERR_REDIRECT,
    APPEND_STDOUT,
    APPEND_STDERR;

    public static OperatorType operatorType(String token) {
        switch (token) {
            case ">", "1>":
                return OperatorType.STDOUT_REDIRECT;
            case "2>":
                return OperatorType.STDERR_REDIRECT;
            case ">>", "1>>":
                return OperatorType.APPEND_STDOUT;
            case "2>>" :
                return OperatorType.APPEND_STDERR;
        }
        return null;
    }
}
