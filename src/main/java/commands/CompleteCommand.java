package commands;

import parser.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteCommand implements Command {
    private static Map<String, List<String>> map = new HashMap<>();

    @Override
    public void execute(CommandLine commandLine) throws Exception {
    }

    private void print(String command) {
        try {
            List<String> list = map.get(command);
            for (String s : list) {
                System.out.println(s);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}
