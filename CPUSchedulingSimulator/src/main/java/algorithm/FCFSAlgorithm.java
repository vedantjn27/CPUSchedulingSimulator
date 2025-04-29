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

public class FCFSAlgorithm implements SchedulingAlgorithm {
    private List<GanttChartEntry> ganttChart;
    
    @Override
    public List<Process> schedule(List<Process> processes) {
        // Create a deep copy of the processes list to avoid modifying the original
        List<Process> result = new ArrayList<>();
        for (Process p : processes) {
            result.add(new Process(p));
        }
        
        // Sort by arrival time
        Collections.sort(result, Comparator.comparing(Process::getArrivalTime));
        
        ganttChart = new ArrayList<>();
        int currentTime = 0;
        
        for (Process p : result) {
            // If there's a gap between processes, advance the time
            if (p.getArrivalTime() > currentTime) {
                currentTime = p.getArrivalTime();
            }
            
            int startTime = currentTime;
            currentTime += p.getBurstTime();
            
            // Record the process execution in the Gantt chart
            ganttChart.add(new GanttChartEntry(p.getId(), p.getName(), startTime, currentTime));
            
            // Update process metrics
            p.setCompletionTime(currentTime);
            p.calculateTurnaroundTime();
            p.calculateWaitingTime();
        }
        
        return result;
    }
    
    @Override
    public String getName() {
        return "First-Come, First-Served (FCFS)";
    }
    
    @Override
    public List<GanttChartEntry> getGanttChartData() {
        return ganttChart;
    }
}