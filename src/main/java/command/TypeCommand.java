package command;

import shell.PathSearch;

public class TypeCommand implements Command{
    @Override
    public void execute(String[] args) throws Exception {
        if (Types.isBuiltin(args[1])){
            System.out.println(args[1] + " is a shell builtin");
        }else{
            String result = PathSearch.searchInDirs(args[1]);
            System.out.println(result);
        }
    }
}
