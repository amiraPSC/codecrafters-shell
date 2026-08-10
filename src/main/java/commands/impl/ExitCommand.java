package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        System.exit(0);
    }
}
