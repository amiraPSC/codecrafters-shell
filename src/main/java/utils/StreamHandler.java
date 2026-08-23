package utils;

import executors.ExecutionContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StreamHandler {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void connect(Process process, boolean isBackground, ExecutionContext context)throws IOException{
        Future<?> inputFuture = null;

        if (context.getStdin() != System.in) {
            inputFuture = connectInput(process, context);
        }

        Future<?> outputFuture = connectOutput(process, context);
        Future<?> errorFuture = connectError(process, context);

        try {
            if (!isBackground){
                process.waitFor();
                List<Future<?>> futures = new ArrayList<>();
                if (inputFuture != null) futures.add(inputFuture);
                futures.add(outputFuture);
                futures.add(errorFuture);
                ConcurrencyUtils.waitAll(futures);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            shutdown();
        }
    }

    public Future<?> connectInput(Process process, ExecutionContext context){
        return executorService.submit(() -> {
            try {
                context.getStdin().transferTo(process.getOutputStream());
            }catch (IOException e){
                throw new CompletionException(e);
            }finally {
                closeQuietlyAsCompletion(process.getOutputStream());
            }
        });
    }

    public Future<?> connectOutput(Process process, ExecutionContext context){
        return executorService.submit(() -> {
           try {
               process.getInputStream().transferTo(context.getStdout());
           }catch (IOException e){
               throw new CompletionException(e);
           }finally {
               closeIfPiped(context.getStdout());
           }
        });
    }

    public Future<?> connectError(Process process, ExecutionContext context){
        return executorService.submit(() -> {
            try {
                process.getErrorStream().transferTo(context.getStderr());
            }catch (IOException e){
                throw new CompletionException(e);
            }finally {
                closeIfPiped(context.getStderr());
            }
        });
    }

    public List<Future<?>> connectErrors(List<Process> processes, ExecutionContext context){
        List<Future<?>> futures = new ArrayList<>();
        for (Process process : processes) {
            futures.add(connectError(process, context));
        }
        return futures;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private static void closeIfPiped(OutputStream stream){
        if (stream instanceof PipedOutputStream pos) {
            closeQuietlyAsCompletion(pos);
        }
    }

    private static void closeQuietlyAsCompletion(OutputStream stream){
        try {
            stream.close();
        }catch (IOException e){
            throw new CompletionException(e);
        }
    }
}
