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
import java.util.List;

public interface SchedulingAlgorithm {
    List<Process> schedule(List<Process> processes);
    String getName();
    
    // For Gantt chart visualization
    List<GanttChartEntry> getGanttChartData();
}