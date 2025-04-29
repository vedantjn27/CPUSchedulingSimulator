# test_algorithms.py
from models.process import Process
from algorithms.fcfs import fcfs
from algorithms.sjf import sjf
from algorithms.priority import priority_scheduling
from algorithms.round_robin import round_robin

def test_algorithms():
    # Create test processes
    p1 = Process(1, 0, 5, 2)  # pid, arrival, burst, priority
    p2 = Process(2, 1, 3, 1)
    p3 = Process(3, 2, 8, 3)
    processes = [p1, p2, p3]
    
    print("Testing FCFS...")
    executed, gantt = fcfs(processes.copy())
    print("FCFS Gantt Chart:", gantt)
    
    print("\nTesting SJF...")
    executed, gantt = sjf(processes.copy())
    print("SJF Gantt Chart:", gantt)
    
    print("\nTesting Priority...")
    executed, gantt = priority_scheduling(processes.copy())
    print("Priority Gantt Chart:", gantt)
    
    print("\nTesting Round Robin...")
    executed, gantt = round_robin(processes.copy(), 2)
    print("RR Gantt Chart:", gantt)
    
    print("\nAll tests completed successfully!")

if __name__ == "__main__":
    test_algorithms()