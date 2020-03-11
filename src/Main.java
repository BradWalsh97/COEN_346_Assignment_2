import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.String.format;

public class Main {
    //Global semaphores that will allow control of execution of threads
//    static Semaphore rrBlock = new Semaphore(1);
//    static Semaphore spfBlock = new Semaphore(1);
    //Global queues used in scheduler
    static ArrayList<Process> waitingQueue = new ArrayList<>(); //waiting queue
    static ArrayList<Process> readyQueue = new ArrayList<>(); //ready queue

    public static void main(String[] args) throws IOException {


        //Start by getting the input file and creating an array consisting of all the light bulbs
        File file = new File(Paths.get("input.txt").toAbsolutePath().toString());
        BufferedReader in = new BufferedReader(new FileReader(file));
        int processCount = 1; //starts at 1 since scheduler is Considered p0
        Scheduler scheduler = new Scheduler(new Process(0, 0, 0));


        //Read data from file and populate the waitingQueue
        String[] splitInputArray;
        String str;


        //start by creating a process object for each item in the text file
        while ((str = in.readLine()) != null) {
            splitInputArray = str.split("\\s", 2); //split arrival time and burst time
            waitingQueue.add(
                    new Process(processCount,
                            Integer.parseInt(splitInputArray[0].trim()),
                            Integer.parseInt(splitInputArray[1].trim())));

            processCount++;
        }
//        for (Process p :waitingQueue) {
//            System.out.println("Process: " + p.getPID() + " "+ p.getArrivalTime());
//        }
        Collections.sort(waitingQueue, new SortByArrival());
        System.out.println("=========Processes=========");
        for (Process p :waitingQueue) {
            System.out.println("Process: " + p.getPID() + " Arrival Time: "+ p.getArrivalTime() + " Run Time: "+ p.getRunTime());
        }
        System.out.println("================Process Execution=================");
        scheduler.start();
        /*
        //Now that its populated we want to start running our threads.
        //todo: remove following comment if required.
        //The following code (unless otherwise stated can be considered the scheduler.
        //If time had permitted, it would be implemented as its own class.
        Process scheduleProcess = new Process(0, 0, 0);
        //create process schedule
        //Scheduler scheduler = new Scheduler(scheduleProcess);

         */

    }

    public static class Scheduler extends Thread {
        //        ArrayList<Process> waitingQueue = new ArrayList<>(); //waiting queue
//        ArrayList<Process> readyQueue = new ArrayList<>(); //ready queue
//    private List<String> log = new ArrayList<>();
//    private ArrayList<Process> waitingQueue = new ArrayList<>();
//    BufferedWriter outputWriter;
//    int currentTime;
//
//    Scheduler(){
//        //create output stream to write the log to
//        FileWriter outputFile = null;
//
//        try {
//            outputFile = new FileWriter("output.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        outputWriter = new BufferedWriter(outputFile);
//    }
//
//    public void addProcess(Process process) {
//        waitingQueue.add(process);
//    }
//    public void start() {
//        //start by sorting the waitingQueue based on arrival time
//        Collections.sort(waitingQueue, new SortByDuration());
//        System.out.println(waitingQueue);
//
//        for (int i = 0; i < waitingQueue.size(); i++)
//            while(currentTime > waitingQueue.get(i+1).getArrivalTime()){
//                roundRobinOnce(waitingQueue.get(i));
//        }
//
//
//    }
//    public void roundRobinOnce(Process process){
//        if (process.getRunTime() > 0.01){
//            process.setRunTime(process.getRunTime() * 0.1); //set new time to 10% of old time
//        }
//
        int processID;
        int arrivalTime;
        double runTime;
        double currentTime;
        int i = 0;
        int j = 0;

        public Scheduler(Process p) {
            this.processID = p.getPID();
            this.arrivalTime = p.getArrivalTime();
            this.runTime = p.getRunTime();
            currentTime = 0;
        }
        private void oneRoundRobinRound(Process p){
            p.setStatus(ProcessStatus.RESUMED);
            printProcessStatus(p);
            p.setRunTime(p.getRunTime() * 0.9);
            currentTime+= (waitingQueue.get(i).getRunTime()/0.9)*0.1;
            p.setStatus(ProcessStatus.PAUSED);

            printProcessStatus(p);

            //check if runtime is less than 0.01. If so, the process is finished.
            if(p.getRunTime() <= 0.01)
                p.setStatus(ProcessStatus.FINISHED);
        }
        private void shortestFirst(Process p){
            p.setStatus(ProcessStatus.RESUMED);
            printProcessStatus(p);
            p.setRunTime(p.getRunTime() * 0.9);
            currentTime+= (readyQueue.get(i).getRunTime()/0.9)*0.1;
            p.setStatus(ProcessStatus.PAUSED);
            printProcessStatus(p);

            //check if runtime is less than 0.01. If so, the process is finished.
            if(p.getRunTime() <= 0.01)
                p.setStatus(ProcessStatus.FINISHED);
        }

        private void printProcessStatus(Process p){
            System.out.println("Time " + currentTime + ", Proccess "
                    + p.getPID() + ", " + waitingQueue.get(i).getStatus()
            + ", remaining time " + format("%.3f",waitingQueue.get(i).getRunTime()));
        }
        private void printWaitTimes(){
            System.out.println("=======Wait Times=======");
            for (Process p : waitingQueue) {
                System.out.println("Proccess " + p.getPID() + " wait time: " + p.getWaitTime());
            }
        }

        @Override
        public void run() {

            //first we run round robin until the end of the waitQueue
            while(true){
                //in the while loop, we run the process (and decrement by 10%) until the next arrival time arrives
                if(i + 1 == waitingQueue.size()) {//this signals end of waiting queue
                    waitingQueue.get(i).setStatus(ProcessStatus.STARTED);
                    printProcessStatus(waitingQueue.get(i));
                    oneRoundRobinRound(waitingQueue.get(waitingQueue.size() - 1)); //thus run last one once
                    readyQueue.add(waitingQueue.get(i));//add process to ready queue
                    break;
                }
                else if(waitingQueue.get(i).getStatus().equals("Finished")) {
                    printProcessStatus(waitingQueue.get(i)); //print that the process is done
                    i++; //if the process is done, we move on to the next one
                }
                else if(currentTime == waitingQueue.get(i).getArrivalTime()) {
                    waitingQueue.get(i).start();
                    waitingQueue.get(i).setStatus(ProcessStatus.STARTED);
                    printProcessStatus(waitingQueue.get(i));
                    oneRoundRobinRound(waitingQueue.get(i));
                }
                else if(currentTime < waitingQueue.get(i+1).getArrivalTime() && currentTime >= waitingQueue.get(i).getArrivalTime()) {
                    //waitingQueue.get(i).setStatus(ProcessStatus.RESUMED);
                    oneRoundRobinRound(waitingQueue.get(i));
                }
                else if(currentTime > waitingQueue.get(i).getArrivalTime()){
                    readyQueue.add(waitingQueue.get(i));//add process to ready queue
                    i++;//move on to next process in waiting queue
                }

                else
                    currentTime++;

                //increment wait time in ready queue
                for (int j = i; j < waitingQueue.size() ; j++)
                    waitingQueue.get(j).setWaitTime(waitingQueue.get(j).getWaitTime() + 1);

//                waitingQueue.get(i).start();
//                waitingQueue.get(i).setStatus(ProcessStatus.STARTED);
//                if (i + 1 == waitingQueue.size()) {
//                    currentTime++;
//                    break; //we've reached the end of the waiting queue, now we go to shortest first
//                }
//                System.out.println("Time " + currentTime + ", Proccess " + (i + 1) + ", Started");
//                while (currentTime < waitingQueue.get(i + 1).getRunTime()) { //add try catch to detect end of list
//                    currentTime++;
////                    if(!waitingQueue.get(i).getStatus().equals(ProcessStatus.STARTED))
////                        waitingQueue.get(i).setStatus(ProcessStatus.STARTED);
////                    else;
//
//
//                }
            }//end round robin

            //now we run the shortest first.
            //The ready queue first needs to be sorted based on the shortest remaining run times.
            Collections.sort(readyQueue, new SortByDuration());

            //first we run round robin until the end of the waitQueue
            while(true){
                //in the while loop, we run the process (and decrement by 10%) until its done, then go to the next process
                if(j + 1 == readyQueue.size()) {//this signals end of ready queue
                    System.out.println("IF 1");
                    readyQueue.get(j).setStatus(ProcessStatus.RESUMED);
                    printProcessStatus(readyQueue.get(j));
                    shortestFirst(readyQueue.get(readyQueue.size() - 1)); //thus run last one once
                    break;
                }
                else if(readyQueue.get(j).getStatus().equals("Finished")) {
                    System.out.println("IF 2");
                    printProcessStatus(readyQueue.get(j)); //print that the process is done
                    j++; //if the process is done, we move on to the next one
                }
                else {
                    System.out.println("IF 3");
                    shortestFirst(readyQueue.get(j));
                }


//                //increment wait time in ready queue
//                for (int j = i; j < waitingQueue.size() ; j++)
//                    waitingQueue.get(j).setWaitTime(waitingQueue.get(j).getWaitTime() + 1);

            }

            //now that its sorted





            printWaitTimes();
        }
    }
}
