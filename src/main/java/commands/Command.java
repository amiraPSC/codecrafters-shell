package commands;

import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;

public interface Command {
    void execute(CommandNode node, ExecutionContext context) throws IOException;
}
