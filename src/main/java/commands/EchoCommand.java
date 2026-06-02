package commands;

import parser.CommandLine;
import parser.OperatorParser;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        if (!OperatorParser.haveOperator(commandLine)){
            System.out.println(String.join(" ", commandLine.getArgs()));
        }
    }
}
