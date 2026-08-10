package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

public class UnknownCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        context.getWriter().println(node.getCommand() + ": command not found");
    }
}
