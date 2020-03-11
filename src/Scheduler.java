//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class Scheduler implements Runnable {
//    ArrayList<Process> waitingQueue = new ArrayList<>(); //waiting queue
//    ArrayList<Process> readyQueue = new ArrayList<>(); //ready queue
////    private List<String> log = new ArrayList<>();
////    private ArrayList<Process> waitingQueue = new ArrayList<>();
////    BufferedWriter outputWriter;
////    int currentTime;
////
////    Scheduler(){
////        //create output stream to write the log to
////        FileWriter outputFile = null;
////
////        try {
////            outputFile = new FileWriter("output.txt");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        outputWriter = new BufferedWriter(outputFile);
////    }
////
////    public void addProcess(Process process) {
////        waitingQueue.add(process);
////    }
////    public void start() {
////        //start by sorting the waitingQueue based on arrival time
////        Collections.sort(waitingQueue, new SortByDuration());
////        System.out.println(waitingQueue);
////
////        for (int i = 0; i < waitingQueue.size(); i++)
////            while(currentTime > waitingQueue.get(i+1).getArrivalTime()){
////                roundRobinOnce(waitingQueue.get(i));
////        }
////
////
////    }
////    public void roundRobinOnce(Process process){
////        if (process.getRunTime() > 0.01){
////            process.setRunTime(process.getRunTime() * 0.1); //set new time to 10% of old time
////        }
////    }
//
//
//        int processID;
//        int arrivalTime;
//        double runTime;
//        int currentTime;
//
//        public Scheduler(Process p){
//            this.processID = p.getPid();
//            this.arrivalTime = p.getArrivalTime();
//            this.runTime = p.getRunTime();
//            currentTime = 0;
//        }
//
//        @Override
//        public void run() {
//            //first we run round robin
//            for (int i = 0; i < waitingQueue.size(); i++){
//                waitingQueue.get(i).start();
//                waitingQueue.get(i).setStatus(ProcessStatus ps = Process.RUNNING);
//                while(currentTime >= waitingQueue.get(i+i).getRunTime()) { //add try catch to detect enf of list
//                    if(waitingQueue.get(i).status() != "Running")
//                        waitingQueue.get(i).resume();
//                }
//
//
//        }
//
//    }
//}
