package command;

public class EchoCommand implements Command{
    @Override
    public void execute(String[] args) throws Exception {
        for (int i = 1; i < args.length; i++){
            System.out.print(args[i] + " ");
        }
        System.out.println();
    }
}
