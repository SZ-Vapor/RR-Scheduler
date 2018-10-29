import java.util.Comparator;

public class SortByArrivalTime implements Comparator<Process> {

    public int compare(Process p1, Process p2) {
        return p1.getArrivalTime() - p2.getArrivalTime();
    }
}

