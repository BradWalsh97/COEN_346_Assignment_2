import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.String.format;

public class Main {
    //queues used to hold the processes
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


        //start by creating a process object for each item in the text file and
        //provide the info for that process
        while ((str = in.readLine()) != null) {
            splitInputArray = str.split("\\s", 2); //split arrival time and burst time
            waitingQueue.add(
                    new Process(processCount,
                            Integer.parseInt(splitInputArray[0].trim()),
                            Integer.parseInt(splitInputArray[1].trim())));

            processCount++;
        }
        //uncomment for debuggin
//        for (Process p :waitingQueue) {
//            System.out.println("Process: " + p.getPID() + " "+ p.getArrivalTime());
//        }
        Collections.sort(waitingQueue, new SortByArrival());

        //print all processes in waiting queue
        System.out.println("=========Processes=========");
        for (Process p :waitingQueue) {
            System.out.println("Process: " + p.getPID() + " Arrival Time: "+ p.getArrivalTime() + " Run Time: "+ p.getRunTime());
        }

        //begin process execution/simulation
        System.out.println("================Process Execution=================");
        scheduler.start();
    }

    public static class Scheduler extends Thread {

        double currentTime;
        int i = 0;
        int j = 0;

        public Scheduler(Process p) {
            currentTime = 0;
        }
        private void oneRoundRobinRound(Process p){
            p.setStatus(ProcessStatus.RESUMED); //resume the process
            printProcessStatus(p);
            p.setRunTime(p.getRunTime() * 0.9); //simulate execution
            currentTime+= (waitingQueue.get(i).getRunTime()/0.9)*0.1;
            p.setStatus(ProcessStatus.PAUSED);
            printProcessStatus(p);

            //check if runtime is less than 0.01. If so, the process is finished.
            if(p.getRunTime() <= 0.01)
                p.setStatus(ProcessStatus.FINISHED);
        }
        private void shortestFirst(Process p){
            p.setStatus(ProcessStatus.RESUMED);
            printReadyQueueProcessStatus(p);
            currentTime+= ((readyQueue.get(j).getRunTime())*0.1);
            p.setRunTime(p.getRunTime() * 0.9);
            p.setStatus(ProcessStatus.PAUSED);
            printReadyQueueProcessStatus(p);

            //check if runtime is less than 0.01. If so, the process is finished.
            if(p.getRunTime() <= 0.01)
                p.setStatus(ProcessStatus.FINISHED);
        }
        private void printProcessStatus(Process p){
            System.out.println("Time " + format("%.3f",currentTime) + ", Process "
                    + p.getPID() + ", " + waitingQueue.get(i).getStatus()
            + ", remaining time " + format("%.3f",waitingQueue.get(i).getRunTime()));
        }
        private void printReadyQueueProcessStatus(Process p){
            System.out.println("Time " + format("%.3f",currentTime) + ", Process "
                    + p.getPID() + ", " + readyQueue.get(j).getStatus()
                    + ", remaining time " + format("%.3f",readyQueue.get(j).getRunTime()));
        }
        private void printWaitTimes(){
            /* Waiting time is defined as the total time idle in both ready and waiting queues
             * between arrival and finish times.*/
            System.out.println("=======Wait Times=======");
            for (Process p : waitingQueue) {
                System.out.println("Proccess " + p.getPID() + " wait time: " + format("%.2f", p.getWaitTime()));
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
                    waitingQueue.get(i).setWaitTime(currentTime - ((double) waitingQueue.get(i).getArrivalTime()) - waitingQueue.get(i).getTotalRunTime());
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
            }//end round robin

            //now we run the shortest first.
            //The ready queue first needs to be sorted based on the shortest remaining run times.
            Collections.sort(readyQueue, new SortByDuration());

            //first we run round robin until the end of the waitQueue
            while(true){
                //in the while loop, we run the process (and decrement by 10%) until its done, then go to the next process

                if(j + 1 == readyQueue.size() && (readyQueue.get(j).getStatus().equals("Finished"))) {//this signals end of ready queue
                    readyQueue.get(j).setWaitTime(currentTime - ((double) readyQueue.get(j).getArrivalTime()) - readyQueue.get(j).getTotalRunTime());
                    break;
                }
                else if(j + 1 == readyQueue.size()) {//this signals end of ready queue
                    //System.out.println("IF 1");
                    readyQueue.get(j).setStatus(ProcessStatus.RESUMED);
                    printReadyQueueProcessStatus(readyQueue.get(j));
                    shortestFirst(readyQueue.get(readyQueue.size() - 1)); //thus run last one once
                }
                else if(readyQueue.get(j).getStatus().equals("Finished")) {
                   // System.out.println("IF 2");
                    readyQueue.get(j).setWaitTime(currentTime - ((double) readyQueue.get(j).getArrivalTime()) - readyQueue.get(j).getTotalRunTime());
                    printReadyQueueProcessStatus(readyQueue.get(j)); //print that the process is done
                    j++; //if the process is done, we move on to the next one
                }
                else {
                    //System.out.println("IF 3");
                    shortestFirst(readyQueue.get(j));
                }
            }
            printWaitTimes();
        }
    }
}
