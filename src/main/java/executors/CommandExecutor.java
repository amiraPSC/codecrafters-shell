package executors;

import parser.nodes.impl.CommandNode;

public interface CommandExecutor {
    void execute(CommandNode commandNode, ExecutionContext context) throws Exception;
}
