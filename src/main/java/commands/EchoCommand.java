package commands;

import parser.CommandLine;
import parser.OperatorHandler;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        OperatorHandler operatorHandler = new OperatorHandler(commandLine);
        if (!operatorHandler.haveOperator()){
            System.out.println(String.join(" ", commandLine.getArgs()));
        }else  {
            operatorHandler.handleStandersRedirection();
        }
    }
}
