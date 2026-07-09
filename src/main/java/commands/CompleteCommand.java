package commands;

import parser.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteCommand implements Command {
    private static Map<String, List<String>> map = new HashMap<>();

    @Override
    public void execute(CommandLine commandLine) throws Exception {
        List<String> args = commandLine.getArgs();
        if (args.get(1).equals("-p")){
            print(args.get(2));
        }
    }

    private void print(String command) {
        if (map.containsKey(command)) {
            List<String> list = map.get(command);
            for (String s : list) {
                System.out.println(s);
            }
        }else {
            System.out.println(String.format("complete: %s: no completion specification", command));
        }
    }
}
