package commands.impl;

import parser.Parser;
import utils.PathScanning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class EchoCommand extends RedirectableCommand {
    @Override
    protected void executeNormally(Parser parser) throws Exception {
        System.out.println(String.join(" ", parser.getTokens()));
    }

    protected void stdoutRedirect(Parser parser, boolean isAppend){
        List<String> tokens = parser.getTokens();
        String line = String.join(" ", tokens.subList(1,tokens.size()));
        Path path = PathScanning.createFile(parser).toPath();
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

    protected void stderrRedirect(Parser parser, boolean isAppend){
        File file = PathScanning.createFile(parser);
        List<String> tokens = parser.getTokens();
        System.out.println(String.join(" ", tokens.subList(1, tokens.size())));
    }
}
