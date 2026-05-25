package command;

import shell.PathSearch;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CdCommand implements Command{
    @Override
    public void execute(String[] args) throws Exception {
        if (args[1].equals("~")) {
            PathSearch.setCurrentDir(Paths.get(System.getProperty("user.home")));
            return;
        }
        Path path = Paths.get(args[1]);
        if (!path.isAbsolute()){
            path = Paths.get(PathSearch.getCurrentDir().toString(), args[1]).normalize();
        }
        if (path.toFile().exists()){
            if (args[1].contains("../")){
                int count = countOccurrences(args[1], "../");
                PathSearch.walkLevels(count);
            }else {
                PathSearch.setCurrentDir(path);
            }
        }else {
            System.out.println("cd: " + args[1] + ": No such file or directory");
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
