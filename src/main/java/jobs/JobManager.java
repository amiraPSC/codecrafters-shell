package jobs;

import utils.ProcessExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JobManager{
    private static int nextJobNumber = 1;
    private static List<Job> jobs = new ArrayList<>();

    public Job addJob(List<String> args) throws IOException {
        Process process = startProcess(args);
        int jobNum = setJobNumber();
        long pid = process.pid();

        Job job = new Job(jobNum, pid, args, process);
        jobs.add(job);

        return job;
    }

    public void printJobInformation(Job job){
        String line = String.format("[%1$d] %2$d", job.getJobNum(), job.getPid());
        System.out.printf(line + "\n");
    }

    public void printStatusJobs(){
        for (Job job : jobs){
            String line = statusLine(job);
            System.out.printf(line + "\n");
        }
    }

    public void removeCompletedJobs(){
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()){
            Job job = iterator.next();
            if (job.getJobStatus() == JobStatus.DONE) {
                iterator.remove();
            }
        }
    }

    private String statusLine(Job job){
        StringBuilder builder = new StringBuilder();

        builder.append("[" + job.getJobNum() + "]");
        markerOfSpecialJobs(job, builder);
        builder.append("  ");
        builder.append(job.getStringStatus());
        builder.append(job.getCommand());
        if (job.getJobStatus() == JobStatus.RUNNING){
            builder.append(" &");
        }

        return builder.toString();
    }

    private void markerOfSpecialJobs(Job job, StringBuilder builder){
        if (jobs.getLast().equals(job)){
            builder.append("+");
        }else if (jobs.get(jobs.size()-2).equals(job)){
            builder.append("-");
        }else {
            builder.append(" ");
        }
    }

    private Process startProcess(List<String> args) throws IOException {
        return ProcessExecutor.startInBackground(args);
    }

    private int setJobNumber(){
        return nextJobNumber++;
    }
}
