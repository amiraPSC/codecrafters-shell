package command;

public class ExitCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        System.exit(0);
    }
}
