package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class DeclareCommand implements Command {
    private Map<String, String> parameters;

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        String option = node.getArgs().getFirst();

        switch (option) {
            case "-p":
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
