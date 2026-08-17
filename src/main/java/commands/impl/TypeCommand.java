package commands.impl;

import commands.Command;
import commands.CommandType;
import executors.ExecutionContext;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

import java.io.IOException;

public class TypeCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        String firstArg = node.getArgs().getFirst();
        if (CommandType.isBuiltin(firstArg)){
            context.getWriter().println(firstArg + " is a shell builtin");
        }else {
            context.getWriter().println(PathScanning.getExecutablePath(firstArg));
        }
    }
}
