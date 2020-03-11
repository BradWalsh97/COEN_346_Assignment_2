import java.io.BufferedWriter;

public class Process extends Thread{

    private int pid;
    private ProcessStatus status;
    private int arrivalTime;
    private double runTime;

    private int waitTime; //amount of time process spends in waitingQueue

    private int counter = 0;
    private final Object lock = new Object();
    private boolean paused = false;
    //private Scheduler observerScheduler;
    private BufferedWriter writer;
    public Process( int id, int arrivalTime, int runTime) {
        this.arrivalTime = arrivalTime;
        this.runTime = runTime;
        this.pid = id;
        this.setStatus(ProcessStatus.WAITING);
        this.writer = writer;

        if (arrivalTime == 1) {
            this.status = ProcessStatus.READY;
        } else {
            this.status = ProcessStatus.WAITING;
        }
    }

    @Override
    public void run() {

    }

    private Thread thread = new Thread(() -> {
        while(true){
            synchronized (lock) {
                if(paused) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    });
    public int getWaitTime() {
        return waitTime;
    }
    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    public double getRunTime() {
        return runTime;
    }
    public void setRunTime(double runTime){
        this.runTime = runTime;
    }
    public int getArrivalTime(){
        return this.arrivalTime;
    }
    public void setArrivalTime(int a){
        this.arrivalTime = a;
    }
    public int getPID() {
        return pid;
    }
    public void setPID(int pid) {
        this.pid = pid;
    }

    public void setStatus(ProcessStatus stat){
        this.status = stat;
    }
    public String getStatus(){
        return this.status.toString();
    }

}
