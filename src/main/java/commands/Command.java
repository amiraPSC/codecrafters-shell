package commands;

import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

public interface Command {
    void execute(CommandNode node, ExecutionContext context) throws Exception;
}
