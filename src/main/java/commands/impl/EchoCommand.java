package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;

public class EchoCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        context.getWriter().println(String.join(" ", node.getArgs()));
    }
}
