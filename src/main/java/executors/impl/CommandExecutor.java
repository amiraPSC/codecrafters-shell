package executors.impl;

import commands.Command;
import commands.CommandFactory;
import commands.CommandType;
import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.NodeExecutor;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;
import utils.ProcessExecutor;
import utils.StreamHandler;

import java.io.IOException;

public class CommandExecutor implements NodeExecutor<CommandNode> {

    @Override
    public ExecutionResult execute(CommandNode node, ExecutionContext context, boolean isBackground) throws IOException {
        ExecutionResult result = new ExecutionResult();
        result = result.withCommands(node.getCommandWithArgs());

        Command cmd = CommandFactory.getCommand(node.getCommand());

        if (CommandType.isBuiltin(node.getCommand())){
            cmd.execute(node,context);
        }else {
            if (!PathScanning.existsInPath(node.getCommand())){
                cmd.execute(node, context);
                result = result.withExitCode(1);
                return result;
            }

            StreamHandler streamHandler = new StreamHandler();

            Process process = ProcessExecutor.startProcess(node.getCommandWithArgs());
            streamHandler.connect(process, isBackground, context);
            result = result.withProcess(process);
        }

        return result;
    }
}
