package commands;

import commands.enums.CommandsTypes;

public class CommandFactory {
    public static Command getCommand(String command){
        CommandsTypes type = CommandsTypes.getType(command);
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
            case COMPLETE -> {
                return new CompleteCommand();
            }
            case EXIT -> {
                return new ExitCommand();
            }
        }
        return new UnknownCommand();
    }
}
