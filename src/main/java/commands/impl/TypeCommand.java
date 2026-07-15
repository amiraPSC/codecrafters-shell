package commands.impl;

import commands.Command;
import commands.CommandsTypes;
import parser.Parser;
import utils.PathScanning;

public class TypeCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        String arg1 = parser.getTokens().get(0);
        if (CommandsTypes.isBuiltin(arg1)){
            System.out.println(arg1 + " is a shell builtin");
        }else{
            String result = PathScanning.getExecutablePath(arg1);
            System.out.println(result);
        }
    }
}
