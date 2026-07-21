package jobs;

public class Job{
    private int jobNum;
    private long pid;
    private Process process;

    public Job(int jobNum, long pid, Process process) {
        this.jobNum = jobNum;
        this.pid = pid;
        this.process = process;
    }

    int getJobNum() {
        return jobNum;
    }

    long getPid() {
        return pid;
    }

    Process getProcess() {
        return process;
    }
}
