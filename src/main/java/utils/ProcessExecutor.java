package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ProcessExecutor {
    public List<String> executeProcess(List<String> args, Map<String, String> env) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Map<String, String> environment = processBuilder.environment();
        environment.putAll(env);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            process.waitFor();
            return reader.lines().toList();
        }
    }

    public static Process startInBackground(List<String> args) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.inheritIO();
        Process process = processBuilder.start();

        return process;
    }
}
