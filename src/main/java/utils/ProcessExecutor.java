package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ProcessExecutor {
    public List<String> executeProcess(List<String> args) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            process.waitFor();
            return reader.lines().toList();
        }
    }
}
