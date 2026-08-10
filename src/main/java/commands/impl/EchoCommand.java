package commands.impl;

import Executebale.RedirectableCommand;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class EchoCommand extends RedirectableCommand {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        context.getWriter().println(String.join(" ", node.getArgs()));
    }

    @Override
    protected void executeNormally(Parser2 parser2) throws Exception {
        System.out.println(String.join(" ", parser2.getTokens()));
    }

    protected void stdoutRedirect(Parser2 parser2, boolean isAppend){
        List<String> tokens = parser2.getTokens();
        String line = String.join(" ", tokens.subList(1,tokens.size()));
        Path path = PathScanning.createFile(parser2).toPath();
        try {
            if (!isAppend) {
                Files.writeString(path, line + '\n');
            }else{
                Files.writeString(path, line + '\n', StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    protected void stderrRedirect(Parser2 parser2, boolean isAppend){
        File file = PathScanning.createFile(parser2);
        List<String> tokens = parser2.getTokens();
        System.out.println(String.join(" ", tokens.subList(1, tokens.size())));
    }
}
