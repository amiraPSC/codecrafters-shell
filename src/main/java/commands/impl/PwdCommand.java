package commands.impl;

import commands.Command;
import parser.Parser;
import utils.PathScanning;

public class PwdCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        System.out.println(PathScanning.getCurrentDir());
    }
}
