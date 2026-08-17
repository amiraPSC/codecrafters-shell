package executors;

import parser.nodes.Node;

import java.io.IOException;

public interface NodeExecutor<T extends Node> {
    ExecutionResult execute(T node, ExecutionContext context, boolean isBackground) throws IOException;
}
