package executors.impl.pipeline;

import commands.CommandType;
import executors.ExecutionContext;
import executors.ExecutionResult;
import parser.nodes.Node;
import parser.nodes.impl.CommandNode;
import parser.nodes.impl.PipelineNode;
import utils.ConcurrencyUtils;
import utils.PathScanning;
import utils.ProcessExecutor;
import utils.StreamHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public class ExternalPipelineStrategy implements PipelineStrategy {

    @Override
    public Optional<ExecutionResult> tryExecute(PipelineNode node, ExecutionContext context, boolean isBackground) throws IOException {
        List<CommandNode> commands = flattenIfAllExternal(node);
        if (commands == null){
            return Optional.empty();
        }

        return Optional.of(execute(commands, context, isBackground));
    }

    private ExecutionResult execute(List<CommandNode> cmds, ExecutionContext context, boolean isBackground) throws IOException {
        StreamHandler streamHandler = new StreamHandler();

        List<ProcessBuilder> builders = new ArrayList<>();
        for (CommandNode command : cmds) {
            builders.add(ProcessExecutor.createProcessBuilder(command.getCommandWithArgs()));
        }

        List<Process> processes = ProcessBuilder.startPipeline(builders);
        Process lastProcess = processes.getLast();

        ExecutionResult result = new ExecutionResult()
                .withProcess(lastProcess)
                .withCommands(cmds.getLast().getCommandWithArgs());

        Future<?> outputFuture = streamHandler.connectOutput(lastProcess, context);
        List<Future<?>> errorFutures = streamHandler.connectErrors(processes, context);

        if (!isBackground){
            try {
                for (Process process : processes) {
                    process.waitFor();
                }
                List<Future<?>> futures = new ArrayList<>(errorFutures);
                futures.add(outputFuture);
                ConcurrencyUtils.waitAll(futures);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

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
            if (!isExternalCommand(pn.getLeft())) {
                return null;
            }
            commandsList.add((CommandNode) pn.getLeft());
            current = pn.getRight();
        }

        if (!isExternalCommand(current)) {
            return null;
        }
        commandsList.add((CommandNode) current);

        return commandsList;
    }

    private boolean isExternalCommand(Node node) {
        return node instanceof CommandNode command
                && !CommandType.isBuiltin(command.getCommand())
                && PathScanning.existsInPath(command.getCommand());
    }
}
