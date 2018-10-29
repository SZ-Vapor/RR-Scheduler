public class Process {

    String processName;
    int arrivalTime;
    int burstTime;
    boolean running;
    int waitTime = 0;
    int turnaroundTime = 0;
    int completedTime;

    public Process(String processName, int arrivalTime, int burstTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    private Process(String processName, int arrivalTime, int burstTime, boolean running, int waitTime, int turnaroundTime) {
        this.processName = processName;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.running = running;
        this.waitTime = waitTime;
        this.turnaroundTime = turnaroundTime;
    }

    String getProcessName() {
        return processName;
    }

    int getArrivalTime() {
        return arrivalTime;
    }

    int getBurstTime() {
        return burstTime;
    }

    boolean isRunning() {
        return running;
    }

    void setRunningTrue() {
        running = true;
    }

    void setRunningFalse() {
        running = false;
    }

    void addWaitTime() {
        waitTime++;
    }

    int getWaitTime() {
        return waitTime;
    }

    void addTurnaroundTime() {
        turnaroundTime++;
    }

    int getTurnaroundTime() {
        return turnaroundTime;
    }

    void reduceBurstTime() {
        if (burstTime > 0) {
            burstTime--;
        }
    }

    void setCompletedTime(int time) {
        completedTime = time;
    }

    int getCompletedTime() {
        return completedTime;
    }

}
