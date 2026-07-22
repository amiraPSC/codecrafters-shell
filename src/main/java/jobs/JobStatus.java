package jobs;

public enum JobStatus {
    RUNNING,
    STOPPED;

    static JobStatus getJobStatus(Process process){
        if (process.isAlive()) return RUNNING;
        return STOPPED;
    }

    String getStatusName(){
        if (this == RUNNING){
            return "Running";
        }
        return "Stopped";
    }
}
