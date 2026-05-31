package command;

import shell.PathSearch;

import java.util.ArrayList;
import java.util.Arrays;

public class UnknownCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        var list = new ArrayList<String>();
        list.add(commandLine.getCommand());
        list.addAll(Arrays.asList(commandLine.getArgs()));
        String[] args = list.toArray(new String[0]);

        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s);
        }

        String resultSearch = PathSearch.searchInDirs(commandLine.getCommand());
        if (!resultSearch.contains("not found")){
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            Process process = processBuilder.start();
            System.err.print(args.toString());
            process.getInputStream().transferTo(System.out);
            process.waitFor();
        }else {
            System.out.println(builder.toString().trim() + ": command not found");
        }
    }
}
