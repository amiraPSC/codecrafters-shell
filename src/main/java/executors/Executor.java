package executors;

import commands.Command;
import commands.CommandFactory;
import commands.CommandType;
import jobs.Job;
import jobs.JobManager;
import parser.lexer.TokenType;
import parser.nodes.Node;
import parser.nodes.impl.BackgroundNode;
import parser.nodes.impl.CommandNode;
import parser.nodes.impl.PipelineNode;
import parser.nodes.impl.RedirectionNode;
import utils.PathScanning;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    private boolean isBackground = false;
    private Process process;
    private List<String> commands = new ArrayList<>();

    public int execute(Node node) throws Exception {
        ExecutionContext context = new ExecutionContext();
        return execute(node, context);
    }

    private int execute(Node node, ExecutionContext context) throws Exception {
        if (node instanceof CommandNode c)
            executeCommand(c, context);
        else if (node instanceof BackgroundNode b)
            executeBackground(b, context);
        else if (node instanceof PipelineNode p)
            executePipeline(p, context);
        else if (node instanceof RedirectionNode r)
            executeRedirect(r, context);
        return 0;
    }

    private int executeBackground(BackgroundNode backgroundNode, ExecutionContext context) throws Exception {
        isBackground = true;
        execute(backgroundNode.getChild(), context);
        JobManager jobManager = new JobManager();
        Job job = jobManager.addJob(process, commands);
        jobManager.printJobInformation(job, context);
        return 0;
    }

    private int executePipeline(PipelineNode pipelineNode, ExecutionContext context) throws Exception {
        if (pipelineNode.getLeft() instanceof CommandNode left
                && pipelineNode.getRight() instanceof CommandNode right
                && !CommandType.isBuiltin(left.getCommand())
                && !CommandType.isBuiltin(right.getCommand())
                && PathScanning.existsInPath(left.getCommand())
                && PathScanning.existsInPath(right.getCommand())) {
            return executeExternalPipeline(left, right, context);
        }

        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);

        ExecutionContext leftContext = context.withOutput(pipeOut);
        ExecutionContext rightContext = context.withInput(pipeIn);

        Thread leftThread = new Thread(() -> {
            try {
                execute(pipelineNode.getLeft(), leftContext);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }finally {
                try {
                    pipeOut.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        });

        Thread rightThread = new Thread(() -> {
            try {
                execute(pipelineNode.getRight(), rightContext);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        leftThread.start();
        rightThread.start();

        leftThread.join();
        rightThread.join();

        return 0;
    }

    private int executeExternalPipeline(
            CommandNode left,
            CommandNode right,
            ExecutionContext context) throws Exception {
        List<Process> processes = ProcessBuilder.startPipeline(List.of(
                createProcessBuilder(left),
                createProcessBuilder(right)
        ));

        Process lastProcess = processes.getLast();
        Thread outputThread = new Thread(() -> copy(lastProcess.getInputStream(), context.getStdout()));
        Thread leftErrorThread = new Thread(() -> copy(processes.getFirst().getErrorStream(), context.getStderr()));
        Thread rightErrorThread = new Thread(() -> copy(lastProcess.getErrorStream(), context.getStderr()));

        outputThread.start();
        leftErrorThread.start();
        rightErrorThread.start();

        for (Process process : processes) {
            process.waitFor();
        }

        outputThread.join();
        leftErrorThread.join();
        rightErrorThread.join();
        return 0;
    }

    private void copy(InputStream input, OutputStream output) {
        try {
            input.transferTo(output);
        } catch (IOException ignored) {
        }
    }

    private int executeRedirect(RedirectionNode redirectionNode, ExecutionContext context) throws Exception {
        TokenType type = redirectionNode.getTokenType();

        String file = redirectionNode.getFile();
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
        return execute(redirectionNode.getChild(), newContext);
    }

    private int executeCommand(CommandNode commandNode, ExecutionContext context) throws Exception {
        commands.addAll(commandNode.getCommandWithArgs());
        Command cmd = CommandFactory.getCommand(commandNode.getCommand());

        if (CommandType.isBuiltin(commandNode.getCommand())){
            cmd.execute(commandNode,context);
        }else {
            if (!PathScanning.existsInPath(commandNode.getCommand())){
                cmd.execute(commandNode, context);
                return 1;
            }

            Process process = startProcess(commandNode);
            streamsCopying(process, context);
            this.process = process;
        }

        return 0;
    }

    private void streamsCopying(Process process, ExecutionContext context) throws Exception {
        Thread inputThread = null;

        if (context.getStdin() != System.in) {
            inputThread = new Thread(() -> {
                try {
                    context.getStdin().transferTo(process.getOutputStream());
                } catch (IOException ignored) {
                } finally {
                    try {
                        process.getOutputStream().close();
                    } catch (IOException ignored) {}
                }
            });
            inputThread.start();
        }

        Thread outputThread = new Thread(() -> {
            try {
                process.getInputStream().transferTo(context.getStdout());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (context.getStdout() instanceof PipedOutputStream pos) {
                    try {
                        pos.close();
                    } catch (IOException ignored) {}
                }
            }
        });

        Thread errorThread = new Thread(() -> {
            try {
                process.getErrorStream().transferTo(context.getStderr());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (context.getStderr() instanceof PipedOutputStream pos) {
                    try {
                        pos.close();
                    } catch (IOException ignored) {}
                }
            }
        });

        outputThread.start();
        errorThread.start();


        if (!isBackground) {
            process.waitFor();
            if (inputThread != null) inputThread.join();
            outputThread.join();
            errorThread.join();
        }
    }

    private Process startProcess(CommandNode command) throws IOException {
        return createProcessBuilder(command).start();
    }

    private ProcessBuilder createProcessBuilder(CommandNode command) {
        ProcessBuilder builder = new ProcessBuilder(command.getCommandWithArgs());
        builder.directory(PathScanning.getCurrentDir().toFile());
        return builder;
    }
}
