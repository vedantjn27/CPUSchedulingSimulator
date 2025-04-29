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

public class SRTFAlgorithm implements SchedulingAlgorithm {
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
        int lastProcessId = -1;
        int lastStartTime = 0;
        
        while (completedProcesses < totalProcesses) {
            // Add newly arrived processes to ready queue
            for (Process p : result) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }
            
            // Sort ready queue by remaining time
            Collections.sort(readyQueue, Comparator.comparing(Process::getRemainingTime));
            
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
                    lastProcessId = -1;
                }
                
                currentTime = nextArrival;
                currentProcess = null;
            } else {
                Process shortestRemainingTimeProcess = readyQueue.get(0);
                
                // If process changed, add entry to Gantt chart
                if (currentProcess == null || currentProcess.getId() != shortestRemainingTimeProcess.getId()) {
                    if (currentProcess != null) {
                        ganttChart.add(new GanttChartEntry(currentProcess.getId(), currentProcess.getName(), lastStartTime, currentTime));
                    }
                    currentProcess = shortestRemainingTimeProcess;
                    lastStartTime = currentTime;
                }
                
                // Execute for 1 time unit
                currentTime++;
                shortestRemainingTimeProcess.setRemainingTime(shortestRemainingTimeProcess.getRemainingTime() - 1);
                
                // If process completes
                if (shortestRemainingTimeProcess.getRemainingTime() == 0) {
                    shortestRemainingTimeProcess.setCompletionTime(currentTime);
                    shortestRemainingTimeProcess.calculateTurnaroundTime();
                    shortestRemainingTimeProcess.calculateWaitingTime();
                    readyQueue.remove(shortestRemainingTimeProcess);
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
        return "Shortest Remaining Time First (SRTF) - Preemptive SJF";
    }
    
    @Override
    public List<GanttChartEntry> getGanttChartData() {
        return ganttChart;
    }
}