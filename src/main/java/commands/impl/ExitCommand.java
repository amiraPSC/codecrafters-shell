package commands.impl;

import commands.Command;
import parser.Parser;

public class ExitCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        System.exit(0);
    }
}
