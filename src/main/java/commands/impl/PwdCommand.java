package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

public class PwdCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        context.getWriter().println(PathScanning.getCurrentDir());
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        System.out.println(PathScanning.getCurrentDir());
    }
}
