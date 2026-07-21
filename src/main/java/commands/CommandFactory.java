package commands;

import commands.impl.*;
import parser.Parser;
import utils.PathScanning;

public class CommandFactory {
    public static Command getCommand(Parser parser){
        if (parser.hasBackgroundOperator()){
            return new JobsCommand();
        }

        String command = parser.getCommand();
        CommandsTypes type = CommandsTypes.getType(command);

        if (CommandsTypes.isBuiltin(command)){
            return getBuiltinCommand(type);
        }

        if (PathScanning.existsInPath(command)){
            return new ExternalCommand();
        }

        return new UnknownCommand();
    }

    private static Command getBuiltinCommand(CommandsTypes type){
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
            case JOBS -> {
                return new JobsCommand();
            }
        }
        return new UnknownCommand();
    }
}
