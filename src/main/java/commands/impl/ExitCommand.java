package commands.impl;

import commands.Command;
import parser.CommandLine;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        System.exit(0);
    }
}
