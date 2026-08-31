package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeclareCommand implements Command {
    private static Map<String, String> parameters = new HashMap<>();

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        List<String> args = node.getArgs();
        String option = args.getFirst();

        switch (option) {
            case "-p":
                printDescriptionVariable(args.get(1), context);
                break;
            default:
                recordVariable(option, context);
        }
    }

    private void  recordVariable(String input, ExecutionContext context){
        String[] split = input.split("=");
        boolean isValid = split[0].matches("[a-zA-Z_][a-zA-Z_0-9]*");

        if (isValid && split.length == 2) {
            parameters.put(split[0], split[1]);
        }else {
            context.getWriter().println(String.format("declare: `%s': not a valid identifier", input));
        }
    }

    private void printDescriptionVariable(String variable, ExecutionContext context) {
        PrintWriter out = context.getWriter();

        if (parameters.containsKey(variable)) {
            out.println(String.format("declare -- %1$s=\"%2$s\"", variable, getVariable(variable)));
        }else {
            out.println(String.format("declare: %s: not found", variable));
        }
    }

    public static String getVariable(String variable){
        return parameters.get(variable);
    }

    public static boolean checkVariable(String variable){
        return parameters.containsKey(variable);
    }
}
