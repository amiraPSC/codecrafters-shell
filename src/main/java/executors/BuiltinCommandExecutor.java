package executors;

import commands.Command;
import commands.CommandFactory;
import parser.nodes.impl.CommandNode;

public class BuiltinCommandExecutor implements CommandExecutor {
    @Override
    public void execute(CommandNode commandNode, ExecutionContext context) throws Exception {
        Command cmd = CommandFactory.getCommand(commandNode.getCommand());
        cmd.execute(commandNode,context);
    }
}
