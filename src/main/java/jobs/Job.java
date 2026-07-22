package jobs;

import java.util.List;

public class Job{
    private int jobNum;
    private long pid;
    private List<String> command;
    private Process process;

    public Job(int jobNum, long pid, List<String> command, Process process) {
        this.jobNum = jobNum;
        this.pid = pid;
        this.command = command;
        this.process = process;
    }

    int getJobNum() {
        return jobNum;
    }

    long getPid() {
        return pid;
    }

    String getCommand() {
        StringBuilder line = new StringBuilder();
        for (String s : command){
            line.append(s).append(" ");
        }
        return line.toString().trim();
    }

    String getStatus(){
        JobStatus status = JobStatus.getJobStatus(process);
        String name = status.getStatusName();

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (int i = name.length(); i < 24; i++){
            builder.append(" ");
        }

        return builder.toString();
    }
}
