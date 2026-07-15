package commands.impl;

import commands.Command;
import parser.Parser;
import utils.PathScanning;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExternalCommand extends RedirectableCommand {
    @Override
    public void execute(Parser parser) throws Exception {
        super.execute(parser);
    }

    protected void executeNormally(Parser parser) throws Exception {
        List<String> args = parser.getArgsWithCommand();

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(PathScanning.getCurrentDir().toFile());
        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out);
        process.waitFor();
    }

    protected void stdoutRedirect(Parser parser, boolean isAppend){
        List<String> tokens = parser.getTokens();
        File file = PathScanning.createFile(parser);
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

    protected void stderrRedirect(Parser parser, boolean isAppend){
        List<String> tokens = parser.getTokens();
        File file = PathScanning.createFile(parser);
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

}
