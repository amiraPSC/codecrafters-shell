package parser;

import commands.Types;
import utils.PathSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class OperatorParser {
    public static boolean haveOperator(CommandLine commandLine) {
        var list = commandLine.getArgs();
        boolean result = list.contains(">") || list.contains("1>");
        return result;
    }

    public static void handleStdoutRedirection(CommandLine commandLine) {
        List<String> args = commandLine.getArgs();
        String command = commandLine.getCommand();
        Types commandType = Types.getType(command);

        int indexOfOperator = (args.indexOf(">") != -1) ? args.indexOf(">") : args.indexOf("1>");
        File file = new File(args.get(indexOfOperator + 1));

        if (commandType == Types.UNKNOWN) {
            handelExternalExecutables(commandLine, indexOfOperator, file);
        } else {
            handelEchoCommand(commandLine, indexOfOperator, file);
        }
    }

    private static void handelExternalExecutables(CommandLine commandLine, int indexOfOperator, File file) {
        List<String> args = commandLine.getArgs();
        String command = commandLine.getCommand();

        var tokensBeforeOperator = new ArrayList<String>();
        tokensBeforeOperator.add(command);
        tokensBeforeOperator.addAll(args.subList(0, indexOfOperator));

        try(FileOutputStream otf = new FileOutputStream(file, false)) {
            ProcessBuilder processBuilder = new ProcessBuilder(tokensBeforeOperator);
            processBuilder.directory(PathSearch.getCurrentDir().toFile());
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();

            try(BufferedReader bos = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = bos.readLine()) != null){
                    if (line.matches("^[^/\\\\\\\\]+$")){
                        otf.write(line.getBytes());
                        otf.write('\n');
                    }
                }
            }
            process.waitFor();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handelEchoCommand(CommandLine commandLine, int indexOfOperator, File file) {
        List<String> args = commandLine.getArgs();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            if (i == indexOfOperator) break;
            stringBuilder.append(args.get(i) + " ");
        }
        try {
            Files.writeString(file.toPath(), stringBuilder.toString().trim() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
