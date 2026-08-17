package utils;

import executors.ExecutionContext;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StreamHandler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void connect(Process process, boolean isBackground, ExecutionContext context)throws IOException{
        Future<?> inputFuture = null;

        if (context.getStdin() != System.in) {
            inputFuture = getInputFuture(executorService, process, context);
        }

        Future<?> outputFuture = getOutputFuture(executorService, process, context);

        Future<?> errorFuture = getErrorFuture(executorService, process, context);

        try {
            if (!isBackground){
                process.waitFor();
                if (inputFuture != null) inputFuture.get();
                outputFuture.get();
                errorFuture.get();
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();

        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException io) {
                throw io;
            }

            throw new RuntimeException(e.getCause());
        }finally {
            executorService.shutdown();
        }
    }

    private Future<?> getInputFuture(ExecutorService executor, Process process, ExecutionContext context) {
        return executor.submit(() -> {
            try {
                context.getStdin().transferTo(process.getOutputStream());
            } catch (IOException e) {
                throw new CompletionException(e);
            } finally {
                try {
                    process.getOutputStream().close();
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            }
        });
    }

    private Future<?> getOutputFuture(ExecutorService executor, Process process, ExecutionContext context) {

        return executor.submit(() -> {
            try {
                process.getInputStream().transferTo(context.getStdout());
            } catch (IOException e) {
                throw new CompletionException(e);
            } finally {
                if (context.getStdout() instanceof PipedOutputStream pos) {
                    try {
                        pos.close();
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }
            }
        });
    }

    private Future<?> getErrorFuture(ExecutorService executor, Process process, ExecutionContext context) {
        return executor.submit(() -> {
            try {
                process.getErrorStream().transferTo(context.getStderr());
            } catch (IOException e) {
                throw new CompletionException(e);
            } finally {
                if (context.getStderr() instanceof PipedOutputStream pos) {
                    try {
                        pos.close();
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }
            }
        });
    }

    public Future<?> connectOutput(Process process, ExecutionContext context){
        return getOutputFuture(executorService, process, context);
    }

    public List<Future<?>> connectErrors(List<Process> processes, ExecutionContext context) {

        List<Future<?>> futures = new ArrayList<>();

        for (Process process : processes) {
            futures.add(
                    getErrorFuture(executorService, process, context)
            );
        }

        return futures;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
