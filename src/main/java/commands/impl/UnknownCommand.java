package commands.impl;

import commands.Command;
import parser.CommandLine;
import parser.OperatorParser;
import utils.PathScanning;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UnknownCommand implements Command {
    @Override
    public void execute(CommandLine commandLine) throws Exception {
        OperatorParser operatorParser = new OperatorParser(commandLine);
        if (!operatorParser.haveOperator()){
            simpleExternalExecutable(commandLine);
        }else {
            handleStanderRedirection(operatorParser);
        }
    }

    private void simpleExternalExecutable(CommandLine commandLine) throws Exception {
        List<String> args = commandLine.getArgsWithCommand();

        String resultSearch = PathScanning.searchInDirs(commandLine.getCommand());
        if (!resultSearch.contains("not found")) {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.directory(PathScanning.getCurrentDir().toFile());
            Process process = processBuilder.start();
            process.getInputStream().transferTo(System.out);
            process.waitFor();
        } else {
            System.out.println(commandLine.getCommand() + ": command not found");
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
        File file = createFile(operatorParser);
        try{
            ProcessBuilder processBuilder = new ProcessBuilder(tokens);
            processBuilder.directory(PathScanning.getCurrentDir().toFile());
            if (isAppend){
                processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(file));
            }else {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.to(file));
            }
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void stderrRedirect(OperatorParser operatorParser, boolean isAppend){
        List<String> tokens = operatorParser.getTokens();
        File file = createFile(operatorParser);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(tokens);
            processBuilder.directory(PathScanning.getCurrentDir().toFile());
            if (!isAppend) {
                processBuilder.redirectError(ProcessBuilder.Redirect.to(file));
            }else {
                processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(file));
            }
            Process process = processBuilder.start();
            process.getInputStream().transferTo(System.out);
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private File createFile(OperatorParser operatorParser){
        File file = new File(operatorParser.getFileName());
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
}
