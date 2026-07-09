package commands;

import commands.enums.CommandsTypes;
import parser.CommandLine;
import utils.PathScanning;

public class TypeCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        String arg1 = commandLine.getArgs().get(0);
        if (CommandsTypes.isBuiltin(arg1)){
            System.out.println(arg1 + " is a shell builtin");
        }else{
            String result = PathScanning.searchInDirs(arg1);
            System.out.println(result);
        }
    }
}
