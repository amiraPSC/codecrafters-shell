package executors;

import executors.impl.BackgroundExecutor;
import executors.impl.CommandExecutor;
import executors.impl.PipelineExecutor;
import executors.impl.RedirectionExecutor;
import parser.nodes.Node;
import parser.nodes.impl.BackgroundNode;
import parser.nodes.impl.CommandNode;
import parser.nodes.impl.PipelineNode;
import parser.nodes.impl.RedirectionNode;

import java.io.IOException;

public class Executor {
    private final CommandExecutor commandExecutor;
    private final BackgroundExecutor backgroundExecutor;
    private final PipelineExecutor pipelineExecutor;
    private final RedirectionExecutor redirectionExecutor;

    public Executor() {
        this.commandExecutor = new CommandExecutor();
        this.pipelineExecutor = new PipelineExecutor(this);
        this.backgroundExecutor = new BackgroundExecutor(this);
        this.redirectionExecutor = new RedirectionExecutor(this);
    }

    public int execute(Node node) throws Exception {
        ExecutionContext context = new ExecutionContext();
        return execute(node, false, context).getExitCode();
    }

    public ExecutionResult execute(Node node, boolean isBackground, ExecutionContext context) throws IOException {
        if (node instanceof CommandNode c)
            return commandExecutor.execute(c, context, isBackground);
        else if (node instanceof BackgroundNode b)
            return backgroundExecutor.execute(b, context, isBackground);
        else if (node instanceof PipelineNode p)
            return pipelineExecutor.execute(p, context, isBackground);
        else if (node instanceof RedirectionNode r)
            return redirectionExecutor.execute(r, context, isBackground);

        return new ExecutionResult();
    }
}
