package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;

public class UnknownCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        context.getWriter().println(node.getCommand() + ": command not found");
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        System.out.println(parser2.getCommand() + ": command not found");
    }
}
