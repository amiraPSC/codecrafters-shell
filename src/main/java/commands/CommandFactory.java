package commands;

import commands.impl.*;

public class CommandFactory {
    private static HistoryCommand history;

    public static Command getCommand(String command){
        CommandType type = CommandType.getType(command);
        return getBuiltinCommand(type);
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
            case HISTORY -> {
                return history;
            }
        }
        return new UnknownCommand();
    }

    public static HistoryCommand getHistory() {
        return history;
    }

    public static void installationHistory(HistoryCommand history){
        CommandFactory.history = history;
    }
}
