package parser;

import commands.Types;
import utils.PathSearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public class OperatorParser {
    public static boolean haveOperator(CommandLine commandLine) {
        var list = new ArrayList<String>(Arrays.asList(commandLine.getArgs()));
        boolean result = list.contains(">") || list.contains("1>");
        String command = commandLine.getCommand();
        Types commandType = Types.getType(command);

        if (result){
            int indexOfOperator = (list.indexOf(">") != -1) ? list.indexOf(">") : list.indexOf("1>");
            File file = new File(list.get(indexOfOperator + 1));

            if (commandType == Types.UNKNOWN) {
                var list2 = new ArrayList<String>();
                list2.add(commandLine.getCommand());
                list2.addAll(Arrays.asList(commandLine.getArgs()));
                list2.remove(">");
                list2.remove("1>");
                String[] args = list2.toArray(new String[0]);

                Path path = Path.of(list.get(indexOfOperator - 1));

                if (Files.exists(path)) {
                    try(FileOutputStream otf = new FileOutputStream(file, false)) {
                        ProcessBuilder processBuilder = new ProcessBuilder(args);
                        processBuilder.directory(PathSearch.getCurrentDir().toFile());
                        Process process = processBuilder.start();
                        process.getInputStream().transferTo(otf);
                        process.waitFor();
                    }catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println(command + ": " + list2.get(indexOfOperator-1) + ": No such file or directory");
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    if (i == indexOfOperator) break;
                    stringBuilder.append(list.get(i) + " ");
                }
                try {
                    Files.writeString(file.toPath(), stringBuilder.toString().trim());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
