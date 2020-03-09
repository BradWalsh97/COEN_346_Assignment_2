import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler extends Thread {
    private List<String> log = new ArrayList<>();
    private ArrayList<Process> waitingQueue = new ArrayList<>();
    BufferedWriter outputWriter;

    Scheduler(){
        //create output stream to write the log to
        FileWriter outputFile = null;

        try {
            outputFile = new FileWriter("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputWriter = new BufferedWriter(outputFile);
    }

    public void addProcess(Process process) {
        waitingQueue.add(process);
    }
    public void start() {
        //start by sorting the waitingQueue based on arrival time
        Collections.sort(waitingQueue, new SortByDuration());


    }

}
