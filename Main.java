/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosc460_p01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Steven
 */
public class Main {

    static int timePassed = 0;
    static int waitTime = 0;

    static ArrayList<Process> processList = new ArrayList<>();
    static ArrayList<String> processNames = new ArrayList<>();
    static ArrayList<Integer> processAT = new ArrayList<>();
    static ArrayList<Integer> processBT = new ArrayList<>();
    static ArrayList<Integer> processTurnaroundTimes = new ArrayList<>();
    static ArrayList<String> processCompleted = new ArrayList<>();
    static ArrayList<Integer> processWaitingTimes = new ArrayList<>();
    static int quantum;

    public static void main(String args[]) throws InterruptedException {
        quantum = 3;
        addProcess("A", 0, 3);
        addProcess("B", 1, 6);
        addProcess("C", 2, 4);
        addProcess("D", 2, 2);
        addProcess("E", 3, 1);
        addProcess("F", 3, 4);
        addProcess("G", 4, 5);
        addProcess("H", 4, 6);

        for (int i = 0; i < processList.size(); i++) {
            processNames.add(processList.get(i).getProcessName());
            processAT.add(processList.get(i).getArrivalTime());
            processBT.add(processList.get(i).getBurstTime());
            processList.get(i).setRunningFalse();

        }

        Collections.sort(processList, new SortByAT());
        for (int i = 0; i < processList.size(); i++) {

            processList.get(i).setRunningTrue();

            for (int j = 0; j < quantum; j++) {
                timePassed++;

                for (int k = i; k < processList.size(); k++) {
                    if (processList.get(i).isRunning() && timePassed > processList.get(i).getArrivalTime()) {
                        processList.get(k).addTurnaroundTime();
                    }
                }

                for (int k = i; k < processList.size(); k++) {
                    if (!processList.get(k).isRunning() && timePassed > processList.get(k).getArrivalTime()) {
                        processList.get(k).addWaitTime();
                    }
                }

                if (processList.get(i).getBurstTime() != 0) {
                    if (timePassed >= processList.get(i).getArrivalTime()) {
                        processList.get(i).reduceBurstTime();
                        if (processList.get(i).getBurstTime() == 0) {
                            processCompleted.add(processList.get(i).getProcessName());
                            processTurnaroundTimes.add(timePassed);
                            break;
                        }
                    }

                } else {
                    processCompleted.add(processList.get(i).getProcessName());
                    processTurnaroundTimes.add(timePassed);
                    break;
                }

            }
            if (processList.get(i).getBurstTime() > 0) {
                processList.add(processList.get(i));
                //processList.remove(i);
                //i--;

            }
            processList.get(i).setRunningFalse();

            //System.out.println(processList.get(i).getProcessName() + " " + processList.get(i).getArrivalTime() + " " + processList.get(i).getBurstTime());
            //processList.remove(i);
            //i--;
        }

        //System.out.println("Turnaround Times: ");
        System.out.println("Turnaround Times:");
        for (int i = 0; i < processCompleted.size(); i++) {
            System.out.println(processList.get(i).processName + " " + processList.get(i).getTurnaroundTime());

        }

        System.out.println("\nWaiting Times:");
        for (int i = 0; i < processCompleted.size(); i++) {

            System.out.println(processList.get(i).processName + " " + processList.get(i).getWaitTime());
        }
        //System.out.println(timePassed);

        System.out.println();
    }

    static void addProcess(String name, int AT, int BT) {
        Process p = new Process(name, AT, BT);
        processList.add(p);
    }

}
