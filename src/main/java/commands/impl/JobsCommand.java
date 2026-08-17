package commands.impl;

import commands.Command;
import executors.ExecutionContext;
import jobs.Job;
import jobs.JobManager;
import parser.nodes.impl.CommandNode;

import java.io.IOException;
import java.util.List;

public class JobsCommand implements Command {
    @Override
    public void execute(CommandNode node, ExecutionContext context) throws IOException {
        JobManager jobManager = new JobManager();
        if (node.getArgs().isEmpty()){
            jobManager.printStatusJobs(context);
            jobManager.removeCompletedJobs();
            return;
        }

        List<String> lineCommand = node.getCommandWithArgs();
        Job job = jobManager.addJob(lineCommand);
        jobManager.printJobInformation(job, context);
    }
}
