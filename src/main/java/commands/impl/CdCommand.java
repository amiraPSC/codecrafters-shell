package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.nio.file.Path;
import java.nio.file.Paths;

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
