import java.util.Comparator;

class SortByDuration implements Comparator<Process> {

    public int compare(Process a, Process b){
        int aDur = a.getDuration();
        int bDur = b.getDuration();
        return Integer.compare(aDur, bDur);
    }
}

