package commands;

import parser.CommandLine;
import parser.OperatorParser;
import utils.PathSearch;

import java.util.List;

public class UnknownCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        if (!OperatorParser.haveOperator(commandLine)){
            List<String> args = commandLine.getArgsWithCommand();

            String resultSearch = PathSearch.searchInDirs(commandLine.getCommand());
            if (!resultSearch.contains("not found")) {
                ProcessBuilder processBuilder = new ProcessBuilder(args);
                processBuilder.directory(PathSearch.getCurrentDir().toFile());
                Process process = processBuilder.start();
                process.getInputStream().transferTo(System.out);
                process.waitFor();
            } else {
                System.out.println(commandLine.getCommand() + ": command not found");
            }
        }
    }
}
