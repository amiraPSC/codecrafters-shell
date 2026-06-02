package commands;

import parser.CommandLine;

public interface Command {
    void execute(CommandLine commandLine) throws Exception;
}
