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
                recordVariable(option);
        }
    }

    private void  recordVariable(String input){
        String[] split = input.split("=");
        if (split.length == 2) {
            parameters.put(split[0], split[1]);
        }
    }

    private void printDescriptionVariable(String variable, ExecutionContext context) {
        PrintWriter out = context.getWriter();

        if (parameters.containsKey(variable)) {
            out.println(String.format("declare -- %1$s=\"%2$s\"", variable, parameters.get(variable)));
        }else {
            out.println(String.format("declare: %s: not found", variable));
        }
    }
}
