import java.util.Comparator;

class SortByDuration implements Comparator<Process> {

    public int compare(Process a, Process b){
        double aDur = a.getRunTime();
        double bDur = b.getRunTime();
        int compVal = Double.compare(aDur, bDur);
        //return compVal;
        if(compVal != 0)
            return compVal;
        else{
            double aArrival = a.getArrivalTime();
            double bArrival = b.getArrivalTime();
            int compArrival = Double.compare(aArrival, bArrival);
            return compArrival;
        }
    }
}

