package jobs;

import Executebale.ProcessExecutor;
import executors.ExecutionContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JobManager{
    private static List<Job> jobs = new ArrayList<>();

    public Job addJob(List<String> args) throws IOException {
        Process process = startProcess(args);
        int jobNum = getJobNumber();
        long pid = process.pid();

        Job job = new Job(jobNum, pid, args, process);
        jobs.add(job);

        return job;
    }

    public Job addJob(Process process, List<String> tokens) throws IOException {
        int jobNum = getJobNumber();
        long pid = process.pid();

        Job job = new Job(jobNum, pid, tokens, process);
        jobs.add(job);

        return job;
    }

    public void reapCompletedJobs(){
        for (Job job : jobs){
            if (job.getJobStatus() == JobStatus.DONE){
                String line = statusLine(job);
                System.out.printf(line + "\n");
            }
        }
        removeCompletedJobs();
    }

    public void printJobInformation(Job job){
        String line = String.format("[%1$d] %2$d", job.getJobNum(), job.getPid());
        System.out.printf(line + "\n");
    }

    public void printJobInformation(Job job, ExecutionContext context){
        String line = String.format("[%1$d] %2$d", job.getJobNum(), job.getPid());
        context.getWriter().printf(line + "\n");
    }

    public void printStatusJobs(ExecutionContext context){
        PrintWriter writer = context.getWriter();
        for (Job job : jobs){
            String line = statusLine(job);
            writer.printf(line + "\n");
        }
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

    private int getJobNumber(){
        int nextJobNumber;
        if (jobs.isEmpty()){
            nextJobNumber = 1;
        }else {
            nextJobNumber = jobs.size()+1;
        }
        return nextJobNumber;
    }
}
