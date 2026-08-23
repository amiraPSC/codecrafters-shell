package executors.impl;

import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.Executor;
import executors.NodeExecutor;
import executors.impl.pipeline.ExternalPipelineStrategy;
import executors.impl.pipeline.MixedPipelineStrategy;
import executors.impl.pipeline.PipelineStrategy;
import parser.nodes.impl.PipelineNode;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class PipelineExecutor implements NodeExecutor<PipelineNode> {
    private final List<PipelineStrategy> strategies;

    public PipelineExecutor(Executor executor) {
        this.strategies = List.of(
                new ExternalPipelineStrategy(),
                new MixedPipelineStrategy(executor)
        );
    }

    @Override
    public ExecutionResult execute(PipelineNode node, ExecutionContext context, boolean isBackground) throws IOException {
        for (PipelineStrategy strategy : strategies) {
            Optional<ExecutionResult> result = strategy.tryExecute(node, context, isBackground);
            if (result.isPresent()){
                return result.get();
            }
        }

        throw new IllegalStateException("No strategy could handle pipeline: " + node);
    }
}
