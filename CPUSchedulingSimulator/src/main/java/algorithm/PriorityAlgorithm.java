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
import java.util.List;

public class PriorityAlgorithm implements SchedulingAlgorithm {
    private List<GanttChartEntry> ganttChart;
    
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
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        
        while (completed.size() < result.size()) {
            // Add all processes that have arrived to the ready queue
            for (Process p : result) {
                if (p.getArrivalTime() <= currentTime && !readyQueue.contains(p) && !completed.contains(p)) {
                    readyQueue.add(p);
                }
            }
            
            if (readyQueue.isEmpty()) {
                // No process available, advance time to the next arrival
                int nextArrivalTime = Integer.MAX_VALUE;
                for (Process p : result) {
                    if (p.getArrivalTime() > currentTime && p.getArrivalTime() < nextArrivalTime) {
                        nextArrivalTime = p.getArrivalTime();
                    }
                }
                currentTime = nextArrivalTime;
            } else {
                // Find the process with highest priority (lower number = higher priority)
                Process highestPriorityProcess = Collections.min(readyQueue, Comparator.comparing(Process::getPriority));
                
                int startTime = currentTime;
                currentTime += highestPriorityProcess.getBurstTime();
                
                // Record process execution in Gantt chart
                ganttChart.add(new GanttChartEntry(highestPriorityProcess.getId(), highestPriorityProcess.getName(), startTime, currentTime));
                
                // Update process metrics
                highestPriorityProcess.setCompletionTime(currentTime);
                highestPriorityProcess.calculateTurnaroundTime();
                highestPriorityProcess.calculateWaitingTime();
                
                readyQueue.remove(highestPriorityProcess);
                completed.add(highestPriorityProcess);
            }
        }
        
        return completed;
    }
    
    @Override
    public String getName() {
        return "Priority Scheduling (Non-preemptive)";
    }
    
    @Override
    public List<GanttChartEntry> getGanttChartData() {
        return ganttChart;
    }
}