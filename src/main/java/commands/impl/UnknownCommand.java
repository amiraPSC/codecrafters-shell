package commands.impl;

import commands.Command;
import parser.Parser;

public class UnknownCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        System.out.println(parser.getCommand() + ": command not found");
    }
}
