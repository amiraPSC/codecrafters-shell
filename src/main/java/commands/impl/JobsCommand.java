package commands.impl;

import commands.Command;
import jobs.Job;
import jobs.JobManager;
import parser.Parser;

import java.util.List;

public class JobsCommand implements Command {
    @Override
    public void execute(Parser parser) throws Exception {
        if (!parser.hasBackgroundOperator()) return;

        List<String> lineCommand = parser.getArgsWithCommand();
        JobManager jobManager = new JobManager();
        Job job = jobManager.addJob(lineCommand);
        jobManager.printJobInformation(job);
    }
}
