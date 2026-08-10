package commands;

import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;

public interface Command {
    void execute(Parser2 parser2) throws Exception;
    void execute(CommandNode node, ExecutionContext context) throws Exception;
}
