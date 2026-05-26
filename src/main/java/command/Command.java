package command;

public interface Command {
    void execute(CommandLine commandLine) throws Exception;
}
