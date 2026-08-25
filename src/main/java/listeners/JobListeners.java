package listeners;

import jobs.JobManager;

public class JobListeners implements Listener {
    private JobManager jobManager;

    public JobListeners() {
        this.jobManager = new JobManager();
    }

    @Override
    public void event() {
        jobManager.reapCompletedJobs();
    }
}
