package command;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        System.out.println(String.join(" ", commandLine.getArgs()));
    }
}
