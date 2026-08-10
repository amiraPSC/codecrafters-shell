package Executebale;

import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExternalCommand extends RedirectableCommand {
    @Override
    public void execute(CommandNode node, ExecutionContext context) throws Exception {
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        if (parser2.hasPipeOperator()){
            List<Process> processes = ProcessExecutor.startPipeline(parser2);
            Process last = processes.getLast();
            ProcessExecutor.PrintProcessResult(last);

        }else {
            super.execute(parser2);
        }
    }

    protected void executeNormally(Parser2 parser2) throws Exception {
        List<String> args = parser2.getArgsWithCommand();

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(PathScanning.getCurrentDir().toFile());
        Process process = processBuilder.start();
        process.getInputStream().transferTo(System.out);
        process.waitFor();
    }

    protected void stdoutRedirect(Parser2 parser2, boolean isAppend){
        List<String> tokens = parser2.getTokens();
        File file = PathScanning.createFile(parser2);
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

    protected void stderrRedirect(Parser2 parser2, boolean isAppend){
        List<String> tokens = parser2.getTokens();
        File file = PathScanning.createFile(parser2);
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
