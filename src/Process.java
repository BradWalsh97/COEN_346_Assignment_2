import java.io.BufferedWriter;

public class Process {
    private int id;
    private ProcessStatus status;
    private int enterTime;
    private int duration;
    private int counter = 0;
    private final Object lock = new Object();
    private boolean paused = false;
    private Scheduler observerScheduler;
    private BufferedWriter writer;

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
    public Process( int id, int enterTime, int duration) {
        this.enterTime = enterTime;
        this.duration = duration;
        this.id = id;
        this.writer = writer;

        if (enterTime == 1) {
            this.status = ProcessStatus.READY;
        } else {
            this.status = ProcessStatus.WAITING;
        }
    }

    public int getDuration() {
        return duration;
    }
}
