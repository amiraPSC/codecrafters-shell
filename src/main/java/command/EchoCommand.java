package command;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        for (int i = 0; i < commandLine.getArgs().length; i++){
            System.out.print(commandLine.getArgs()[i]);
        }
        System.out.println();
    }
}
