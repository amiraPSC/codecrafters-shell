package commands.impl;

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
                registerCompletionScript(commandLine);
                break;
        }
    }

    private void registerCompletionScript(CommandLine commandLine) {
        List<String> args = commandLine.getArgs();
        String command = commandLine.getCommand();
        StringBuilder script = new StringBuilder();

        script.append("\'");
        for (String arg : args) {
            script.append(arg).append(" ");
        }
        script.deleteCharAt(script.length() - 1);
        script.append("\'");

        map.put(command, script.toString());
    }

    private void print(String command) {
        if (map.containsKey(command)) {
            System.out.println(String.format("complete -C %1$s %2$s", map.get(command), command));
        }else {
            System.out.println(String.format("complete: %s: no completion specification", command));
        }
    }
}
