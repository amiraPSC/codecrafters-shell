package commands.impl;

import commands.Command;
import jobs.Job;
import jobs.JobManager;
import parser.Parser;

import java.util.List;

public class JobsCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        JobManager jobManager = new JobManager();
        if (parser.isEmpty()) {
            jobManager.printStatusJobs();
            jobManager.removeCompletedJobs();
            return;
        }

        List<String> lineCommand = parser.getArgsWithCommand();
        Job job = jobManager.addJob(lineCommand);
        jobManager.printJobInformation(job);
    }
}
