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
        history.add("history");
        for (int i = 0; i < history.size(); i++){
            context.getWriter().println( "    " + (i+1) + "  " + history.get(i));
        }
    }

    public static void addCommand(String line) {
        if (line.trim().equals("history")) return;
        history.add(line);
    }
}
