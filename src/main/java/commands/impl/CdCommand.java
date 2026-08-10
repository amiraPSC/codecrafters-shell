package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CdCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        String firstArg = node.getArgs().getFirst();

        if (firstArg.equals("~")){
            PathScanning.setCurrentDir(Paths.get(System.getenv("HOME")));
            return;
        }

        Path path = Paths.get(firstArg);

        if (!path.isAbsolute()){
            path = Paths.get(PathScanning.getCurrentDir().toString(), firstArg).normalize();
        }

        if (path.toFile().exists()){
            if (firstArg.contains("../")){
                int count = countOccurrences(firstArg, "../");
                PathScanning.walkLevels(count);
            }else {
                PathScanning.setCurrentDir(path);
            }
        }else {
            System.out.println("cd: " + firstArg + ": No such file or directory");
        }
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        String arg1 = parser2.getTokens().get(0);
        if (arg1.equals("~")) {
            PathScanning.setCurrentDir(Paths.get(System.getenv("HOME")));
            return;
        }
        Path path = Paths.get(arg1);
        if (!path.isAbsolute()){
            path = Paths.get(PathScanning.getCurrentDir().toString(), arg1).normalize();
        }
        if (path.toFile().exists()){
            if (arg1.contains("../")){
                int count = countOccurrences(arg1, "../");
                PathScanning.walkLevels(count);
            }else {
                PathScanning.setCurrentDir(path);
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
