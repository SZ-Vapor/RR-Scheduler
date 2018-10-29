
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    static ArrayList<Process> processList = new ArrayList<>();
    static ArrayList<String> original = new ArrayList<String>();
    static ArrayList<String> processNames = new ArrayList<String>();
    static ArrayList<Integer> processATs = new ArrayList<Integer>();
    static ArrayList<Integer> processBTs = new ArrayList<Integer>();
    static ArrayList<String> processCompleted = new ArrayList<>();
    static Map<String, Integer> pName_pTime = new TreeMap<>();

    static int quantum;
    static int timePassed = 0;

    public static void main(String args[]) throws InterruptedException, FileNotFoundException {

        System.out.print("Enter the name of the text file to use (For Example: xyz.txt): ");
        String fileName = new Scanner(System.in).nextLine();
        Scanner usIn = new Scanner(new File(fileName));//text file used as input

        while (usIn.hasNext()) {
            String line = usIn.nextLine();
            String splitLine[] = line.split(" ");
            if (!line.equals("")) {
                if (line.charAt(0) != '#') {
                    if (!line.contains("#")) {
                        if (splitLine.length == 1) {
                            quantum = Integer.parseInt(line);//sets quantum to int found in the last line of the text file
                        }
                        original.add(line);
                    }
                }
            }
        }
        usIn.close();//Close scanner object
        addInfoToLists();

        /**
         * adds new processes to list processList based off information stored
         * in lists from text file
         */
        for (int i = 0; i < processNames.size(); i++) {
            addProcess(processNames.get(i), processATs.get(i), processBTs.get(i));
        }

        for (int i = 0; i < processList.size(); i++) {
            processList.get(i).setRunningFalse();//used in determining waiting time
        }

        Collections.sort(processList, new SortByArrivalTime());
        for (int i = 0; i < processList.size(); i++) {

            while (timePassed < processList.get(i).getArrivalTime()) {//increases timePassed if no processes are currently available.
                timePassed++;
            }
            processList.get(i).setRunningTrue();//waiting time will not be increased

            for (int j = 0; j < quantum; j++) {
                timePassed++;

                for (int k = i; k < processList.size(); k++) {
                    if (!processList.get(k).isRunning() && timePassed > processList.get(k).getArrivalTime()) {
                        processList.get(k).addWaitTime();//adds to all processes who are not running and available's waiting time
                    }
                }

                if (processList.get(i).getBurstTime() != 0) {
                    if (timePassed >= processList.get(i).getArrivalTime()) {
                        processList.get(i).reduceBurstTime();
                        if (processList.get(i).getBurstTime() == 0) {
                            processList.get(i).setCompletedTime(timePassed);//if a process's remaining burst time is 0, add the process to completed process list, and store the time of completion 
                            processCompleted.add(processList.get(i).getProcessName());
                            break;
                        }
                    }
                }
            }
            if (processList.get(i).getBurstTime() > 0) {
                processList.add(processList.get(i));//if a process is not complete, add it to the end of the same arraylist

            }
            processList.get(i).setRunningFalse();
        }

        System.out.println("Turnaround Times:");
        double avgTT;
        double sumTT = 0;
        for (int i = 0; i < processCompleted.size(); i++) {
            pName_pTime.put(processList.get(i).processName, (processList.get(i).getCompletedTime() - processList.get(i).getArrivalTime()));//adds process's name and TT to map to be sorted alphabetically by Key
            sumTT += processList.get(i).getCompletedTime() - processList.get(i).getArrivalTime();//sums all turnaround times
        }
        avgTT = sumTT / processCompleted.size();
        printMap(pName_pTime);
        pName_pTime.clear();//clears map to use it again with waiting times
        System.out.println("AVERAGE TURNAROUND TIME: " + avgTT);

        double avgWT;
        double sumWT = 0;
        System.out.println("\nWaiting Times:");
        for (int i = 0; i < processCompleted.size(); i++) {
            pName_pTime.put(processList.get(i).processName, processList.get(i).getWaitTime());//adds process's name and WT to map to be sorted alphabetically by Key
            sumWT += processList.get(i).getWaitTime();
        }
        printMap(pName_pTime);
        avgWT = sumWT / processCompleted.size();
        System.out.println("AVERAGE WAITING TIME: " + avgWT);

    }

    /**
     * Creates a new process and adds it to list
     *
     * @param name Process's name
     * @param AT Process's arrival time
     * @param BT Process's burst time
     */
    static void addProcess(String name, int AT, int BT) {
        Process p = new Process(name, AT, BT);
        processList.add(p);
    }

    /**
     * Adds information from the text file to lists in order to use determine
     * the quantum, processName, processAT, processBT etc...
     */
    static void addInfoToLists() {
        ArrayList<String> elements = new ArrayList<String>();
        for (int i = 0; i < original.size(); i++) {
            String split[] = original.get(i).split("\\s+");

            for (String line : split) {
                elements.add(line);
            }
        }
        quantum = Integer.parseInt(original.get(original.size() - 1));
        elements.remove(elements.size() - 1);
        original.remove(original.size() - 1);
        for (int i = 0; i < elements.size(); i += 3) {
            processNames.add(elements.get(i));
        }

        for (int i = 1; i < elements.size(); i += 3) {
            processATs.add(Integer.parseInt(elements.get(i)));
        }

        for (int i = 2; i < elements.size(); i += 3) {
            processBTs.add(Integer.parseInt(elements.get(i)));
        }
    }

    /**
     * Prints a sorted map based off key. Alphabetical order of process names
     * names
     *
     * @param <K> Process's name
     * @param <V> Process's wait/turnaround time
     * @param map
     */
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
