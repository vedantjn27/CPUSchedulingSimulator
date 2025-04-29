/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algorithm;

/**
 *
 * @author vedan
 */


public class GanttChartEntry {
    private int processId;
    private String processName;
    private int startTime;
    private int endTime;
    
    public GanttChartEntry(int processId, String processName, int startTime, int endTime) {
        this.processId = processId;
        this.processName = processName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public int getProcessId() {
        return processId;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public int getStartTime() {
        return startTime;
    }
    
    public int getEndTime() {
        return endTime;
    }
    
    public int getDuration() {
        return endTime - startTime;
    }
}