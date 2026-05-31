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
            String programmeName = args[0];
            for (int i = 1; i < args.length; i++){
                String[] subArgs = new String[]{programmeName, args[i]};
                ProcessBuilder processBuilder = new ProcessBuilder(subArgs);
                Process process = processBuilder.start();
                process.getInputStream().transferTo(System.out);
                process.waitFor();
            }
        }else {
            System.out.println(builder.toString().trim() + ": command not found");
        }
    }
}
