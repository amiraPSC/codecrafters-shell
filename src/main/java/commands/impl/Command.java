package commands.impl;

import parser.CommandLine;

public interface Command {
    void execute(CommandLine commandLine) throws Exception;
}
