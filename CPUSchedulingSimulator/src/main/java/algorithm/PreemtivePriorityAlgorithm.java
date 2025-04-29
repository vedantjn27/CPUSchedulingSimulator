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

public class PreemtivePriorityAlgorithm implements SchedulingAlgorithm {
    private List<GanttChartEntry> ganttChart;
    
    @Override
    public List<Process> schedule(List<Process> processes) {
        // Create a deep copy of the processes list
        List<Process> result = new ArrayList<>();
        for (Process p : processes) {
            result.add(new Process(p));
        }
        
        ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> readyQueue = new ArrayList<>();
        
        // Sort by arrival time initially
        Collections.sort(result, Comparator.comparing(Process::getArrivalTime));
        int totalProcesses = result.size();
        
        Process currentProcess = null;
        int lastStartTime = 0;
        
        while (completedProcesses < totalProcesses) {
            // Add newly arrived processes to ready queue
            for (Process p : result) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
            
            // Sort ready queue by priority (lower number = higher priority)
            Collections.sort(readyQueue, Comparator.comparing(Process::getPriority));
            
            if (readyQueue.isEmpty()) {
                // No process available, find next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (Process p : result) {
                    if (p.getArrivalTime() > currentTime && p.getArrivalTime() < nextArrival) {
                        nextArrival = p.getArrivalTime();
                    }
                }
                
                // Add a Gantt chart entry for idle time if needed
                if (currentProcess != null) {
                    ganttChart.add(new GanttChartEntry(currentProcess.getId(), currentProcess.getName(), lastStartTime, currentTime));
                    currentProcess = null;
                }
                
                currentTime = nextArrival;
            } else {
                Process highestPriorityProcess = readyQueue.get(0);
                
                // If process changed, add entry to Gantt chart
                if (currentProcess == null || currentProcess.getId() != highestPriorityProcess.getId()) {
                    if (currentProcess != null) {
                        ganttChart.add(new GanttChartEntry(currentProcess.getId(), currentProcess.getName(), lastStartTime, currentTime));
                    }
                    currentProcess = highestPriorityProcess;
                    lastStartTime = currentTime;
                }
                
                // Find when the next process will arrive or complete
                int nextEventTime = currentTime + highestPriorityProcess.getRemainingTime();
                
                for (Process p : result) {
                    if (p.getArrivalTime() > currentTime && p.getArrivalTime() < nextEventTime && p.getPriority() < highestPriorityProcess.getPriority()) {
                        nextEventTime = p.getArrivalTime();
                    }
                }
                
                int executionTime = nextEventTime - currentTime;
                highestPriorityProcess.setRemainingTime(highestPriorityProcess.getRemainingTime() - executionTime);
                currentTime = nextEventTime;
                
                // If process completes
                if (highestPriorityProcess.getRemainingTime() == 0) {
                    highestPriorityProcess.setCompletionTime(currentTime);
                    highestPriorityProcess.calculateTurnaroundTime();
                    highestPriorityProcess.calculateWaitingTime();
                    readyQueue.remove(highestPriorityProcess);
                    completedProcesses++;
                    
                    // Add final Gantt chart entry for completed process
                    ganttChart.add(new GanttChartEntry(currentProcess.getId(), currentProcess.getName(), lastStartTime, currentTime));
                    currentProcess = null;
                }
            }
        }
        
        // Sort result by ID for consistent output
        Collections.sort(result, Comparator.comparing(Process::getId));
        return result;
    }
    
    @Override
    public String getName() {
        return "Priority Scheduling (Preemptive)";
    }
    
    @Override
    public List<GanttChartEntry> getGanttChartData() {
        return ganttChart;
    }
}