package executors.impl.pipeline;

import executors.ExecutionContext;
import executors.ExecutionResult;
import parser.nodes.impl.PipelineNode;

import java.io.IOException;
import java.util.Optional;

public interface PipelineStrategy {
    Optional<ExecutionResult> tryExecute(PipelineNode node, ExecutionContext context, boolean isBackground) throws IOException;
}
