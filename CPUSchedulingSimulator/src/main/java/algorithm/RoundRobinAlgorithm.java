/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algorithm;

/**
 *
 * @author vedan
 */


import model.Process;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobinAlgorithm implements SchedulingAlgorithm {
    private List<GanttChartEntry> ganttChart;
    private int timeQuantum;
    
    public RoundRobinAlgorithm(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }
    
    @Override
    public List<Process> schedule(List<Process> processes) {
        // Create a deep copy of the processes list
        List<Process> result = new ArrayList<>();
        for (Process p : processes) {
            result.add(new Process(p));
        }
        
        // Sort by arrival time initially
        Collections.sort(result, Comparator.comparing(Process::getArrivalTime));
        
        ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completedProcesses = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        
        // Add first process to ready queue
        if (!result.isEmpty()) {
            if (result.get(0).getArrivalTime() > 0) {
                currentTime = result.get(0).getArrivalTime();
            }
            readyQueue.add(result.get(0));
        }
        
        while (completedProcesses < result.size()) {
            if (readyQueue.isEmpty()) {
                // Find the next process to arrive
                Process nextProcess = null;
                for (Process p : result) {
                    if (p.getRemainingTime() > 0 && p.getArrivalTime() > currentTime) {
                        if (nextProcess == null || p.getArrivalTime() < nextProcess.getArrivalTime()) {
                            nextProcess = p;
                        }
                    }
                }
                
                if (nextProcess != null) {
                    currentTime = nextProcess.getArrivalTime();
                    readyQueue.add(nextProcess);
                } else {
                    // All processes are complete
                    break;
                }
            }
            
            Process currentProcess = readyQueue.poll();
            int startTime = currentTime;
            
            // Determine execution time
            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            currentTime += executionTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
            
            // Add to Gantt chart
            ganttChart.add(new GanttChartEntry(currentProcess.getId(), currentProcess.getName(), startTime, currentTime));
            
            // Add any new arrivals to ready queue
            for (Process p : result) {
                if (p.getArrivalTime() > startTime && p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0 && !readyQueue.contains(p) && p != currentProcess) {
                    readyQueue.add(p);
                }
            }
            
            // Check if process is complete
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                currentProcess.calculateTurnaroundTime();
                currentProcess.calculateWaitingTime();
                completedProcesses++;
            } else {
                // Add back to ready queue if not complete
                readyQueue.add(currentProcess);
            }
        }
        
        // Sort result by ID for consistent output
        Collections.sort(result, Comparator.comparing(Process::getId));
        return result;
    }
    
    @Override
    public String getName() {
        return "Round Robin (Time Quantum = " + timeQuantum + ")";
    }
    
    @Override
    public List<GanttChartEntry> getGanttChartData() {
        return ganttChart;
    }
}