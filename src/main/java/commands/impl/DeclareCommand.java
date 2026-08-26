package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class DeclareCommand implements Command {
    private static Map<String, String> parameters;

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        List<String> args = node.getArgs();
        String option = args.getFirst();

        switch (option) {
            case "-p":
                printDescriptionVariable(args.get(1), context);
                break;
        }
    }

    private void printDescriptionVariable(String variable, ExecutionContext context) {
        PrintWriter out = context.getWriter();

        if (parameters.containsKey(variable)) {
            out.println(variable + " " + parameters.get(variable));
        }else {
            out.printf("declare: %s: not found", variable);
        }
    }
}
