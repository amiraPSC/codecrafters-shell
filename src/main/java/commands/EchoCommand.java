package commands;

import parser.CommandLine;
import parser.OperatorParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class EchoCommand implements Command{
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        OperatorParser operatorParser = new OperatorParser(commandLine);
        if (!operatorParser.haveOperator()){
            System.out.println(String.join(" ", commandLine.getArgs()));
        }else  {
            handleStanderRedirection(operatorParser);
        }
    }

    public void handleStanderRedirection(OperatorParser operatorParser){
        switch (operatorParser.getOperatorType()){
            case STDOUT_REDIRECT ->  stdoutRedirect(operatorParser,false);
            case STDERR_REDIRECT -> stderrRedirect(operatorParser, false);
            case APPEND_STDOUT ->  stdoutRedirect(operatorParser, true);
            case APPEND_STDERR ->  stderrRedirect(operatorParser, true);
        }
    }

    private void stdoutRedirect(OperatorParser operatorParser, boolean isAppend){
        List<String> tokens = operatorParser.getTokens();
        String line = String.join(" ", tokens.subList(1,tokens.size()));
        Path path = createFile(operatorParser).toPath();
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

    private void stderrRedirect(OperatorParser operatorParser, boolean isAppend){
        List<String> tokens = operatorParser.getTokens();
        System.out.println(String.join(" ", tokens.subList(1, tokens.size())));
    }

    private File createFile(OperatorParser operatorParser){
        File file = new File(operatorParser.getFileName());
        try {
            Path path = file.toPath();
            {
                if (!Files.exists(path)){
                    file = Files.createFile(path).toFile();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return file;
    }
}
