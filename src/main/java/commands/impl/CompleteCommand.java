package commands.impl;

import commands.Command;
import parser.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteCommand implements Command {
    private static Map<String, String> map = new HashMap<>();

    @Override
    public void execute(Parser parser) throws Exception {
        List<String> args = parser.getTokens();
        String option = args.get(0);

        switch (option) {
            case "-p":
                print(args.get(1));
                break;
            case "-C":
                registerCompletionScript(args);
                break;
            case "-r":
                removeCompletionScript(parser.getCommand());
                break;
        }
    }

    private void registerCompletionScript(List<String> args) {
        StringBuilder script = new StringBuilder();

        script.append("\'");
        for (String arg : args) {
            if (args.getLast().equals(arg)) break;
            if (args.getFirst().equals(arg)) continue;
            script.append(arg).append(" ");
        }
        script.deleteCharAt(script.length() - 1);
        script.append("\'");

        map.put(args.getLast(), script.toString());
    }

    private void print(String command) {
        if (map.containsKey(command)) {
            System.out.println(String.format("complete -C %1$s %2$s", map.get(command), command));
        }else {
            System.out.println(String.format("complete: %s: no completion specification", command));
        }
    }

    private void removeCompletionScript(String command) {
        if (map.containsKey(command)) {
            map.remove(command);
        }
    }

    public static String getValue(String key){
        return map.get(key);
    }

    public static boolean hasKey(String key){
        return map.containsKey(key);
    }
}
