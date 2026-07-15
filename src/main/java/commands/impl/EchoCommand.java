package commands.impl;

import commands.Command;
import parser.OperatorParser;
import parser.Parser;
import utils.PathScanning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class EchoCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        if (!parser.haveOperator()){
            System.out.println(String.join(" ", parser.getTokens()));
        }else  {
            handleStanderRedirection(parser);
        }
    }

    public void handleStanderRedirection(Parser parser) throws IOException {
        switch (parser.getOperatorType()){
            case STDOUT_REDIRECT ->  stdoutRedirect(parser,false);
            case STDERR_REDIRECT -> stderrRedirect(parser, false);
            case APPEND_STDOUT ->  stdoutRedirect(parser, true);
            case APPEND_STDERR ->  stderrRedirect(parser, true);
        }
    }

    private void stdoutRedirect(Parser parser, boolean isAppend){
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

    private void stderrRedirect(Parser parser, boolean isAppend){
        File file = PathScanning.createFile(parser);
        List<String> tokens = parser.getTokens();
        System.out.println(String.join(" ", tokens.subList(1, tokens.size())));
    }
}
