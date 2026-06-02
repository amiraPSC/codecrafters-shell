package commands;

import parser.CommandLine;
import utils.PathSearch;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CdCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        String arg1 = commandLine.getArgs()[0];
        if (arg1.equals("~")) {
            PathSearch.setCurrentDir(Paths.get(System.getenv("HOME")));
            return;
        }
        Path path = Paths.get(arg1);
        if (!path.isAbsolute()){
            path = Paths.get(PathSearch.getCurrentDir().toString(), arg1).normalize();
        }
        if (path.toFile().exists()){
            if (arg1.contains("../")){
                int count = countOccurrences(arg1, "../");
                PathSearch.walkLevels(count);
            }else {
                PathSearch.setCurrentDir(path);
            }
        }else {
            System.out.println("cd: " + arg1 + ": No such file or directory");
        }
    }

    private int countOccurrences(String pathName, String pattern){
        int count = 0;
        int index = 0;
        while ((index = pathName.indexOf(pattern, index)) != -1){
            count++;
            index += pattern.length();
        }
        return count;
    }
}
