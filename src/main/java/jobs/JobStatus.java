package jobs;

public enum JobStatus {
    RUNNING,
    DONE;

    static JobStatus getJobStatus(Process process){
        if (process.isAlive()) return RUNNING;
        return DONE;
    }

    String getStatusName(){
        if (this == RUNNING){
            return "Running";
        }
        return "Done";
    }
}
