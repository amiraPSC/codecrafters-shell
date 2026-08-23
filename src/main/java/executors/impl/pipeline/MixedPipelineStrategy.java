package executors.impl.pipeline;

import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.Executor;
import parser.nodes.impl.PipelineNode;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MixedPipelineStrategy implements PipelineStrategy{
    private final Executor executor;

    public MixedPipelineStrategy(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Optional<ExecutionResult> tryExecute(PipelineNode node, ExecutionContext context, boolean isBackground) throws IOException {
        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);

        ExecutionContext leftContext = context.withOutput(pipeOut);
        ExecutionContext rightContext = context.withInput(pipeIn);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            Future<ExecutionResult> leftFuture = executorService.submit(() -> {
                        try {
                            return executor.execute(node.getLeft(), isBackground, leftContext);
                        } finally {
                            pipeOut.close();
                        }
                    });

            Future<ExecutionResult> rightFuture =
                    executorService.submit(() -> executor.execute(node.getRight(), isBackground, rightContext));

            leftFuture.get();
            return Optional.of(rightFuture.get());

        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);

        }catch (ExecutionException e){
            throw new RuntimeException(e.getCause());

        }finally {
            executorService.shutdown();
        }
    }
}
