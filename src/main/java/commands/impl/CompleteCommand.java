package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteCommand implements Command {
    private static Map<String, String> map = new HashMap<>();

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        List<String> args = node.getArgs();
        String command = args.getLast();
        String option = args.getFirst();

        switch (option) {
            case "-p":
                print(args.get(1), context);
                break;
            case "-C":
                registerCompletionScript(args);
                break;
            case "-r":
                removeCompletionScript(command);
                break;
        }
    }

    private void registerCompletionScript(List<String> args) {
        StringBuilder script = new StringBuilder();

        for (String arg : args) {
            if (args.getLast().equals(arg)) break;
            if (args.getFirst().equals(arg)) continue;
            script.append(arg).append(" ");
        }
        script.deleteCharAt(script.length() - 1);

        map.put(args.getLast(), script.toString());
    }

    private void print(String command, ExecutionContext context){
        String line;
        if (map.containsKey(command)) {
            line = String.format("complete -C '%1$s' %2$s", map.get(command), command);
        }else {
            line = String.format("complete: %s: no completion specification", command);
        }

        context.getWriter().println(line);
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
