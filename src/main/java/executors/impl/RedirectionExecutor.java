package executors.impl;

import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.Executor;
import executors.NodeExecutor;
import parser.lexer.TokenType;
import parser.nodes.impl.RedirectionNode;

import java.io.FileOutputStream;
import java.io.IOException;

public class RedirectionExecutor implements NodeExecutor<RedirectionNode> {
    private Executor executor;

    public RedirectionExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public ExecutionResult execute(RedirectionNode node, ExecutionContext context, boolean isBackground) throws IOException {
        TokenType type = node.getTokenType();

        String file = node.getFile();
        ExecutionContext newContext;
        switch (type){
            case REDIRECT_OUT ->
                    newContext = context.withOutput(new FileOutputStream(file));

            case REDIRECT_ERR ->
                    newContext = context.withError(new FileOutputStream(file));

            case REDIRECT_APPEND ->
                    newContext = context.withOutput(new FileOutputStream(file, true));

            case REDIRECT_ERR_APPEND ->
                    newContext = context.withError(new FileOutputStream(file, true));

            default -> throw new IllegalStateException("Unexpected redirect type: " + type);
        }
        return executor.execute(node.getChild(), isBackground, newContext);
    }
}
