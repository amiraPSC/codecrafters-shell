package commands;

import Executebale.ExternalCommand;
import commands.impl.*;
import parser.Parser2;
import utils.PathScanning;

public class CommandFactory {
    public static Command getCommand(String command){
        CommandType type = CommandType.getType(command);
        return getBuiltinCommand(type);
    }

    public static Command getCommand(Parser2 parser2){
        if (parser2.hasBackgroundOperator()){
            return new JobsCommand();
        }

        String command = parser2.getCommand();
        CommandType type = CommandType.getType(command);

        if (CommandType.isBuiltin(command)){
            return getBuiltinCommand(type);
        }

        if (PathScanning.existsInPath(command)){
            return new ExternalCommand();
        }

        return new UnknownCommand();
    }

    private static Command getBuiltinCommand(CommandType type){
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
