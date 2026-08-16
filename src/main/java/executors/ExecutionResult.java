package executors;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionResult {
    private Process process;
    private List<String> commands;
    private int exitCode;

    public ExecutionResult(Process process, List<String> commands, int exitCode) {
        this.process = process;
        this.commands = commands;
        this.exitCode = exitCode;
    }

    public ExecutionResult() {
        this(null, new ArrayList<>(), 0);
    }

    public Process getProcess() {
        return process;
    }

    public ExecutionResult withProcess(Process process) {
        return new ExecutionResult(process, commands, exitCode);
    }

    public List<String> getCommands() {
        return commands;
    }

    public ExecutionResult withCommands(List<String> commands) {
        return new ExecutionResult(process, commands, exitCode);
    }

    public int getExitCode() {
        return exitCode;
    }

    public ExecutionResult withExitCode(int exitCode) {
        return new ExecutionResult(process, commands, exitCode);
    }
}
