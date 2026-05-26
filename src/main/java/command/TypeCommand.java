package command;

import shell.PathSearch;

public class TypeCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        String arg1 = commandLine.getArgs()[0];
        if (Types.isBuiltin(arg1)){
            System.out.println(arg1 + "is a shell builtin");
        }else{
            String result = PathSearch.searchInDirs(arg1);
            System.out.println(result);
        }
    }
}
