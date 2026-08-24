package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import parser.Parser;
import parser.nodes.Node;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryCommand implements Command {
    private static List<String> history =  new ArrayList<>();

    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        List<String> args = node.getArgs();
        history.add(node.getCommand() + " " + args.getFirst());

        if (!node.getArgs().isEmpty()) {
            int n = Integer.getInteger(args.getFirst());
            printNHistory(n, context);
        }else {
            printAllHistory(context);
        }
    }

    public static void addCommand(String line) {
        if (line.startsWith("history")) return;
        history.add(line);
    }

    private void printAllHistory(ExecutionContext context) {
        for (int i = 0; i < history.size(); i++){
            formatPrint(i, context);
        }
    }

    private void printNHistory(int n, ExecutionContext context) {
        int limit = history.size() - n;
        for (int i = limit - 1; i < history.size(); i++){
            formatPrint(i, context);
        }
    }

    private void formatPrint(int i, ExecutionContext context) {
        context.getWriter().println( "    " + (i+1) + "  " + history.get(i));
    }
}
