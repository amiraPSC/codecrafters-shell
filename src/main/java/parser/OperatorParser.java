package parser;

import commands.Types;
import utils.PathSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperatorParser {
    public static boolean haveOperator(CommandLine commandLine) {
        var list = commandLine.getArgs();
        boolean result = list.contains(">") || list.contains("1>");
        String command = commandLine.getCommand();
        Types commandType = Types.getType(command);

        if (result){
            int indexOfOperator = (list.indexOf(">") != -1) ? list.indexOf(">") : list.indexOf("1>");
            File file = new File(list.get(indexOfOperator + 1));

            if (commandType == Types.UNKNOWN) {
                var tokensBeforeOperator = new ArrayList<String>();
                tokensBeforeOperator.add(commandLine.getCommand());
                tokensBeforeOperator.addAll(commandLine.getArgs());
                for (int i = 0; i < tokensBeforeOperator.size(); i++) {
                    if (i > indexOfOperator) tokensBeforeOperator.remove(i);
                }

                Path path = Path.of(list.get(indexOfOperator - 1));

                if (Files.exists(path)) {
                    try(FileOutputStream otf = new FileOutputStream(file, false)) {
                        ProcessBuilder processBuilder = new ProcessBuilder(tokensBeforeOperator);
                        processBuilder.directory(PathSearch.getCurrentDir().toFile());
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
                }else {
                    System.out.println(command + ": " + tokensBeforeOperator.get(indexOfOperator) + ": No such file or directory");
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    if (i == indexOfOperator) break;
                    stringBuilder.append(list.get(i) + " ");
                }
                try {
                    Files.writeString(file.toPath(), stringBuilder.toString().trim() + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
