/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vedan
 */


public class Process {
    private int id;
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int remainingTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    
    public Process(int id, String name, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
    
    // Deep copy constructor
    public Process(Process other) {
        this.id = other.id;
        this.name = other.name;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.priority = other.priority;
        this.remainingTime = other.remainingTime;
        this.completionTime = other.completionTime;
        this.waitingTime = other.waitingTime;
        this.turnaroundTime = other.turnaroundTime;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    public int getBurstTime() {
        return burstTime;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public int getRemainingTime() {
        return remainingTime;
    }
    
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    
    public int getCompletionTime() {
        return completionTime;
    }
    
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    
    public int getWaitingTime() {
        return waitingTime;
    }
    
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    
    public void calculateTurnaroundTime() {
        this.turnaroundTime = this.completionTime - this.arrivalTime;
    }
    
    public void calculateWaitingTime() {
        this.waitingTime = this.turnaroundTime - this.burstTime;
    }
    
    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", completionTime=" + completionTime +
                ", waitingTime=" + waitingTime +
                ", turnaroundTime=" + turnaroundTime +
                '}';
    }
}
