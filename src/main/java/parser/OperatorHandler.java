package parser;

import commands.Types;
import commands.UnknownCommand;
import utils.PathSearch;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class OperatorHandler {
    private OperatorParser operatorParser;
    private CommandLine commandLine;
    Types commandType;

    public OperatorHandler(CommandLine commandLine) {
        this.commandLine = commandLine;
        commandType = Types.getType(commandLine.getCommand());
        operatorParser = new OperatorParser(commandLine);
    }

    public boolean haveOperator() {
        return operatorParser.haveOperator(commandLine.getArgsWithCommand());
    }

    private void stdoutRedirect(){
        if (commandType == Types.UNKNOWN){
            try (FileOutputStream otf = new FileOutputStream(operatorParser.getFile(), false)) {
                ProcessBuilder processBuilder = new ProcessBuilder(operatorParser.getTokens());
                processBuilder.directory(PathSearch.getCurrentDir().toFile());
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
                Process process = processBuilder.start();

                try (BufferedReader bos = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = bos.readLine()) != null) {
                        if (line.matches("^[^/\\\\\\\\]+$")) {
                            otf.write(line.getBytes());
                            otf.write('\n');
                        }
                    }
                }
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (commandType == Types.ECHO) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < operatorParser.getTokens().size(); i++) {
                stringBuilder.append(operatorParser.getTokens().get(i) + " ");
            }
            try {
                Files.writeString(operatorParser.getFile().toPath(), stringBuilder.toString().trim() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stderrRedirect(){
        if (commandType == Types.UNKNOWN){
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(operatorParser.getTokens());
                processBuilder.directory(PathSearch.getCurrentDir().toFile());
                processBuilder.redirectError(ProcessBuilder.Redirect.to(operatorParser.getFile()));
                Process process = processBuilder.start();
                process.getInputStream().transferTo(System.out);
                process.waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } else if (commandType == Types.ECHO) {
            System.out.println(String.join(" ", operatorParser.getTokens()));
        }
    }

    public void handleStandersRedirection() {
        switch (operatorParser.getOperatorType()){
            case STDOUT_REDIRECT ->  stdoutRedirect();
            case STDERR_REDIRECT -> stderrRedirect();
        }
    }
}
