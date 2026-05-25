package command;

import shell.PathSearch;

public class UnknownCommand implements Command {
    @Override
    public void execute(String[] args) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (String s : args) {
            builder.append(s).append(" ");
        }

        String resultSearch = PathSearch.searchInDirs(args[0]);
        if (!resultSearch.contains("not found")){
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            Process process = processBuilder.start();
            process.getInputStream().transferTo(System.out);
            process.waitFor();
        }else {
            System.out.println(builder.toString() + ": command not found");
        }
    }
}
