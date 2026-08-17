package executors.impl;

import commands.CommandType;
import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.Executor;
import executors.NodeExecutor;
import parser.nodes.Node;
import parser.nodes.impl.CommandNode;
import parser.nodes.impl.PipelineNode;
import utils.PathScanning;
import utils.ProcessExecutor;
import utils.StreamHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PipelineExecutor implements NodeExecutor<PipelineNode> {
    private final Executor executor;

    public PipelineExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public ExecutionResult execute(PipelineNode node, ExecutionContext context, boolean isBackground) throws IOException {
        List<CommandNode> allCommands = flattenIfAllExternal(node);
        if (allCommands != null) {
            return executeExternalPipeline(allCommands, isBackground, context);
        }

        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);

        ExecutionContext leftContext = context.withOutput(pipeOut);
        ExecutionContext rightContext = context.withInput(pipeIn);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<ExecutionResult> leftFuture =
                executorService.submit(() -> {
                    try {
                        return executor.execute(node.getLeft(), isBackground, leftContext);
                    } finally {
                        pipeOut.close();
                    }
                });

        Future<ExecutionResult> rightFuture =
                executorService.submit(() -> executor.execute(node.getRight(), isBackground, rightContext));

        try {
            ExecutionResult leftResult = leftFuture.get();
            ExecutionResult rightResult = rightFuture.get();

            return rightResult;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);

        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());

        } finally {
            executorService.shutdown();
        }
    }

    private ExecutionResult executeExternalPipeline(List<CommandNode> cmds, boolean isBackground, ExecutionContext context) throws IOException {
        StreamHandler streamHandler = new StreamHandler();

        List<ProcessBuilder> builders = new ArrayList<>();
        for (CommandNode command : cmds) builders.add(ProcessExecutor.createProcessBuilder(command.getCommandWithArgs()));

        List<Process> processes = ProcessBuilder.startPipeline(builders);

        ExecutionResult result = new ExecutionResult();

        Process lastProcess = processes.getLast();
        List<String> lastCommands = cmds.getLast().getCommandWithArgs();

        result = result.withProcess(lastProcess);
        result = result.withCommands(lastCommands);

        Future<?> outputFuture = streamHandler.connectOutput(lastProcess, context);

        List<Future<?>> errorFutures = streamHandler.connectErrors(processes, context);

        if (!isBackground){
            try {
                for (Process process : processes) process.waitFor();

                outputFuture.get();

                for (Future<?> future : errorFutures) future.get();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

            } catch (ExecutionException e) {
                if (e.getCause() instanceof IOException io) {
                    throw io;
                }

                throw new RuntimeException(e.getCause());
            }finally {
                streamHandler.shutdown();
            }
        }

        return result;
    }

    private List<CommandNode> flattenIfAllExternal(PipelineNode pipelineNode) {
        List<CommandNode> commandsList = new ArrayList<>();
        Node current = pipelineNode;

        while (current instanceof PipelineNode pn) {
            if (!(pn.getLeft() instanceof CommandNode left)
                    || CommandType.isBuiltin(left.getCommand())
                    || !PathScanning.existsInPath(left.getCommand())) {
                return null;
            }
            commandsList.add(left);
            current = pn.getRight();
        }

        if (!(current instanceof CommandNode last)
                || CommandType.isBuiltin(last.getCommand())
                || !PathScanning.existsInPath(last.getCommand())) {
            return null;
        }
        commandsList.add(last);

        return commandsList;
    }
}
