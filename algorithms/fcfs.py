# algorithms/fcfs.py  

def fcfs(processes):
    """
    First-Come-First-Served scheduling algorithm.
    
    Args:
        processes (list): List of Process objects
        
    Returns:
        tuple: (executed_processes, gantt_chart_data)
    """
    # Create a copy of processes to avoid modifying the original
    processes = sorted(processes, key=lambda p: p.arrival_time)
    
    current_time = 0
    gantt_chart = []
    executed_processes = []
    
    for process in processes:
        # If the process hasn't arrived yet, advance the time
        if current_time < process.arrival_time:
            current_time = process.arrival_time
        
        # Set start time for this process
        process.start_time = current_time
        
        # Execute the process (runs to completion in FCFS)
        execution_time = process.execute()
        
        # Update the current time
        current_time += execution_time
        
        # Set completion time
        process.completion_time = current_time
        
        # Calculate waiting and turnaround times
        process.calculate_times()
        
        # Add to executed processes
        executed_processes.append(process)
        
        # Add to Gantt chart data
        gantt_chart.append({
            'pid': process.pid,
            'start': process.start_time,
            'end': process.completion_time
        })
    
    return executed_processes, gantt_chart