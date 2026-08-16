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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor {

    public int execute(Node node) throws Exception {
        ExecutionContext context = new ExecutionContext();
        return execute(node, false, context).getExitCode();
    }

    private ExecutionResult execute(Node node, boolean isBackground, ExecutionContext context) throws Exception {
        if (node instanceof CommandNode c)
            return executeCommand(c, isBackground, context);
        else if (node instanceof BackgroundNode b)
            return executeBackground(b, context);
        else if (node instanceof PipelineNode p)
            return executePipeline(p, isBackground, context);
        else if (node instanceof RedirectionNode r)
            return executeRedirect(r, isBackground, context);

        return new ExecutionResult();
    }

    private ExecutionResult executeBackground(BackgroundNode backgroundNode, ExecutionContext context) throws Exception {
        ExecutionResult result = execute(backgroundNode.getChild(), true, context);
        JobManager jobManager = new JobManager();
        Job job = jobManager.addJob(result.getProcess(), result.getCommands());
        jobManager.printJobInformation(job, context);
        return result;
    }

    private ExecutionResult executePipeline(PipelineNode pipelineNode, boolean isBackground, ExecutionContext context) throws Exception {
        List<CommandNode> allCommands = flattenIfAllExternal(pipelineNode);
        if (allCommands != null) {
            return executeExternalPipeline(allCommands, isBackground, context);
        }

        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream(pipeOut);

        ExecutionContext leftContext = context.withOutput(pipeOut);
        ExecutionContext rightContext = context.withInput(pipeIn);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<ExecutionResult> leftFuture =
                executor.submit(() -> {
                    try {
                        return execute(pipelineNode.getLeft(), isBackground, leftContext);
                    } finally {
                        pipeOut.close();
                    }
                });

        Future<ExecutionResult> rightFuture =
                executor.submit(() -> execute(pipelineNode.getRight(), isBackground, rightContext));

        ExecutionResult leftResult = leftFuture.get();
        ExecutionResult rightResult = rightFuture.get();

        executor.shutdown();

        return rightResult;
    }

    private ExecutionResult executeExternalPipeline(List<CommandNode> cmds,
                                                    boolean isBackground, ExecutionContext context) throws Exception {

        List<ProcessBuilder> builders = new ArrayList<>();
        for (CommandNode command : cmds) builders.add(createProcessBuilder(command));

        List<Process> processes = ProcessBuilder.startPipeline(builders);

        ExecutionResult result = new ExecutionResult();

        Process lastProcess = processes.getLast();
        List<String> lastCommands = cmds.getLast().getCommandWithArgs();

        result = result.withProcess(lastProcess);
        result = result.withCommands(lastCommands);

        Thread outputThread = new Thread(() -> copy(lastProcess.getInputStream(), context.getStdout()));

        List<Thread> errorThreads = new ArrayList<>();
        for (Process p : processes) {
            Thread t = new Thread(() -> copy(p.getErrorStream(), context.getStderr()));
            errorThreads.add(t);
            t.start();
        }

        outputThread.start();

        if (!isBackground){
            for (Process process : processes) process.waitFor();

            outputThread.join();
            for (Thread t : errorThreads) t.join();
        }

        return result;
    }

    private void copy(InputStream input, OutputStream output) {
        try {
            input.transferTo(output);
        } catch (IOException ignored) {
        }
    }

    private ExecutionResult executeRedirect(RedirectionNode redirectionNode, boolean isBackground, ExecutionContext context) throws Exception {
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
        return execute(redirectionNode.getChild(), isBackground, newContext);
    }

    private ExecutionResult executeCommand(CommandNode commandNode, boolean isBackground, ExecutionContext context) throws Exception {
        ExecutionResult result = new ExecutionResult();
        result = result.withCommands(commandNode.getCommandWithArgs());

        Command cmd = CommandFactory.getCommand(commandNode.getCommand());

        if (CommandType.isBuiltin(commandNode.getCommand())){
            cmd.execute(commandNode,context);
        }else {
            if (!PathScanning.existsInPath(commandNode.getCommand())){
                cmd.execute(commandNode, context);
                result = result.withExitCode(1);
                return result;
            }

            Process process = startProcess(commandNode);
            streamsCopying(process, isBackground, context);
            result = result.withProcess(process);
        }

        return result;
    }

    private void streamsCopying(Process process, boolean isBackground, ExecutionContext context) throws Exception {
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
