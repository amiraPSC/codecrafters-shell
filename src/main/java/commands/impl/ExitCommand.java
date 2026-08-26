package commands.impl;

import commands.Command;
import commands.CommandFactory;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;

import java.io.IOException;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        HistoryCommand historyCommand = CommandFactory.getHistory();
        historyCommand.saveToHistFile();

        System.exit(0);
    }
}
