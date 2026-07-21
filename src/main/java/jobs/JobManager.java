package jobs;

import org.jline.terminal.Terminal;
import terminal.DisplayManager;
import terminal.TerminalContext;
import utils.ProcessExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JobManager{
    private int nextJobNumber = 1;
    private static List<Job> jobs = new ArrayList<>();

    public Job addJob(List<String> args) throws IOException {
        Process process = startProcess(args);
        int jobNum = setJobNumber();
        long pid = process.pid();

        Job job = new Job(jobNum, pid, process);
        jobs.add(job);

        return job;
    }

    public void printJobInformation(Job job){
        String line = String.format("[%1$d] %2$d", job.getJobNum(), job.getPid());
        System.out.printf(line + "\n");
    }

    private Process startProcess(List<String> args) throws IOException {
        return ProcessExecutor.startInBackground(args);
    }

    private int setJobNumber(){
        int num = nextJobNumber;
        nextJobNumber = nextJobNumber++;
        return num;
    }
}
