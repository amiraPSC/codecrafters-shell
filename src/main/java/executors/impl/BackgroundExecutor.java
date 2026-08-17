package executors.impl;

import executors.ExecutionContext;
import executors.ExecutionResult;
import executors.Executor;
import executors.NodeExecutor;
import jobs.Job;
import jobs.JobManager;
import parser.nodes.impl.BackgroundNode;

import java.io.IOException;

public class BackgroundExecutor implements NodeExecutor<BackgroundNode> {
    private final Executor executor;

    public BackgroundExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public ExecutionResult execute(BackgroundNode node, ExecutionContext context, boolean isBackground) throws IOException {
        ExecutionResult result = executor.execute(node.getChild(), true, context);
        JobManager jobManager = new JobManager();
        Job job = jobManager.addJob(result.getProcess(), result.getCommands());
        jobManager.printJobInformation(job, context);
        return result;
    }
}
