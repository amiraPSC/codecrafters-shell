package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathSearch {
    private static Path currentDir =  Paths.get(System.getProperty("user.dir"));

    public static String searchInDirs(String input){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        for (String path1 : paths){
            Path p = Paths.get(path1, input);
            if (p.toFile().exists() && p.toFile().canExecute()){
                return (input + " is " + p.toFile().getAbsolutePath());
            }
        }
        return (input + ": not found");
    }

    public static void walkLevels(int level){
        Path p = currentDir;
        for (int i = 0; i < level; i++){
            p = p.getParent();
        }
        currentDir = p;
    }

    public static Path getCurrentDir() {
        return currentDir;
    }

    public static void setCurrentDir(Path currentDir) {
        PathSearch.currentDir = currentDir;
    }
}
