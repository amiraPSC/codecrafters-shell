package commands.impl;

import commands.Command;
import commands.CommandType;
import executors.ExecutionContext;
import parser.Parser2;
import parser.nodes.impl.CommandNode;
import utils.PathScanning;

public class TypeCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) {
        String firstArg = node.getArgs().getFirst();
        if (CommandType.isBuiltin(firstArg)){
            context.getWriter().println(firstArg + " is a shell builtin");
        }else {
            context.getWriter().println(PathScanning.getExecutablePath(firstArg));
        }
    }

    @Override
    public void execute(Parser2 parser2) throws Exception {
        String arg1 = parser2.getTokens().get(0);
        if (CommandType.isBuiltin(arg1)){
            System.out.println(arg1 + " is a shell builtin");
        }else{
            String result = PathScanning.getExecutablePath(arg1);
            System.out.println(result);
        }
    }
}
