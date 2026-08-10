package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        System.exit(0);
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        System.exit(0);
    }
}
