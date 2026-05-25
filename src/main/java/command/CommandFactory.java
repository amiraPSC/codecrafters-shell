package command;

public class CommandFactory {
    public static Command getCommand(String command){
        Types type = Types.getType(command);
        switch (type) {
            case CD -> {
                return new CdCommand();
            }
            case PWD -> {
                return new PwdCommand();
            }
            case ECHO -> {
                return new EchoCommand();
            }
            case TYPE -> {
                return new TypeCommand();
            }
            case EXIT -> {
                return new ExitCommand();
            }
        }
        return new UnknownCommand();
    }
}
