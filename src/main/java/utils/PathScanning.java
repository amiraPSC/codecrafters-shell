package utils;

import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class PathScanning {
    private static Path currentDir =  Paths.get(System.getProperty("user.dir"));
    private static String executablePath;

    public static String getExecutablePath(String input){
        if (existsInPath(input)){
            return (input + " is " + executablePath);
        }
        return (input + ": not found");
    }

    public static boolean existsInPath(String input){
        String[] paths = getPaths();

        for (String path1 : paths){
            Path p = Paths.get(path1, input);
            if (p.toFile().exists() && p.toFile().canExecute()){
                executablePath = p.toFile().getAbsolutePath();
                return true;
            }
        }
        return false;
    }

    public static Set<Path> getFilesInDir(Path dir){
        Set<Path> files = new HashSet<>();

        try (Stream<Path> stream = Files.list(dir)){
            for (Path path : stream.toList()){
                files.add(path);
            }
        } catch (IOException e) {}
        return files;
    }

    public static Set<String> listOfPATHs(){
        String[] paths = getPaths();

        Set<String> pathSet = new HashSet<>();

        for (String p : paths) {
            Path p1 = Paths.get(p);
            if (!Files.isDirectory(p1)) continue;
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(p1);
                for (Path p2 : stream) {
                    if(Files.isExecutable(p2) && isExists(p2)){
                        pathSet.add(p2.getFileName().toString());
                    }
                }
            } catch (IOException e) {}
        }
        return pathSet;
    }

    public static File createFile(Parser parser){
        File file = new File(parser.getFileName());
        try {
            Path path = file.toPath();
            if (!Files.exists(path)){
                file = Files.createFile(path).toFile();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return file;
    }

    private static String[] getPaths(){
        String path = System.getenv("PATH");
        String separator = System.getProperty("path.separator");
        String[] paths = path.split(separator);

        return paths;
    }

    public static boolean isExists(Path path){
        return Files.exists(path);
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
        PathScanning.currentDir = currentDir;
    }
}
