/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author vedan
 */


import model.Process;
import java.util.List;

public class SchedulingMetrics {
    public static double calculateAverageWaitingTime(List<Process> processes) {
        if (processes.isEmpty()) {
            return 0;
        }
        
        double totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTime();
        }
        
        return totalWaitingTime / processes.size();
    }
    
    public static double calculateAverageTurnaroundTime(List<Process> processes) {
        if (processes.isEmpty()) {
            return 0;
        }
        
        double totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        
        return totalTurnaroundTime / processes.size();
    }
    
    public static double calculateThroughput(List<Process> processes) {
        if (processes.isEmpty()) {
            return 0;
        }
        
        int maxCompletionTime = 0;
        for (Process p : processes) {
            if (p.getCompletionTime() > maxCompletionTime) {
                maxCompletionTime = p.getCompletionTime();
            }
        }
        
        return (double) processes.size() / maxCompletionTime;
    }
    
    public static Process findLongestWaitingProcess(List<Process> processes) {
        if (processes.isEmpty()) {
            return null;
        }
        
        Process longestWaiting = processes.get(0);
        for (Process p : processes) {
            if (p.getWaitingTime() > longestWaiting.getWaitingTime()) {
                longestWaiting = p;
            }
        }
        
        return longestWaiting;
    }
}
