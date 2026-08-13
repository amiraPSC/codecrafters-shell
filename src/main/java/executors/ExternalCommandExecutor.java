package executors;

import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.io.IOException;

public class ExternalCommandExecutor implements CommandExecutor {
    private Process process;

    @Override
    public void execute(CommandNode commandNode, ExecutionContext context) throws Exception {
        Process process = startProcess(commandNode);
        context.getStdin().transferTo(process.getOutputStream());
        process.getInputStream().transferTo(context.getStdout());
        process.getErrorStream().transferTo(context.getStderr());
        this.process = process;
    }

    public Process getProcess() {
        if (process != null) {
            return process;
        }
        return null;
    }

    private ProcessBuilder createProcessBuilder(CommandNode command) {
        ProcessBuilder builder = new ProcessBuilder(command.getCommandWithArgs());
        builder.directory(PathScanning.getCurrentDir().toFile());
        return builder;
    }

    private Process startProcess(CommandNode command) throws IOException {
        return createProcessBuilder(command).start();
    }
}
