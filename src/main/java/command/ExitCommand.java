package command;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        System.exit(0);
    }
}
