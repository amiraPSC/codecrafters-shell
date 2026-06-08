package commands;

import parser.CommandLine;
import parser.OperatorHandler;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        if (!OperatorHandler.haveOperator(commandLine)){
            System.out.println(String.join(" ", commandLine.getArgs()));
        }else  {
            OperatorHandler.handleStandersRedirection(commandLine);
        }
    }
}
