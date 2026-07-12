package commands.impl;

import commands.Command;
import parser.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompleteCommand implements Command {
    private static Map<String, String> map = new HashMap<>();

    @Override
    public void execute(CommandLine commandLine) throws Exception {
        List<String> args = commandLine.getArgs();
        String command = args.get(0);

        switch (command) {
            case "-p":
                print(args.get(1));
                break;
            case "-C":
                registerCompletionScript(args);
                break;
            default:
                System.out.println(command);
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
}
