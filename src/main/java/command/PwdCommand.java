package command;

import shell.PathSearch;

public class PwdCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        System.out.println(PathSearch.getCurrentDir());
    }
}
