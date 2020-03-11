import java.util.Comparator;

class SortByDuration implements Comparator<Process> {

    public int compare(Process a, Process b){
        double aDur = a.getRunTime();
        double bDur = b.getRunTime();
        return Double.compare(aDur, bDur);
    }
}

