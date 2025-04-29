# algorithms/priority.py

def priority_scheduling(processes):
    """
    Priority scheduling algorithm (non-preemptive).
    Lower priority number means higher priority.
    
    Args:
        processes (list): List of Process objects
        
    Returns:
        tuple: (executed_processes, gantt_chart_data)
    """
    # Create a copy of processes to avoid modifying the original
    processes = sorted(processes, key=lambda p: p.arrival_time)
    n = len(processes)
    
    # Initialize variables
    current_time = 0
    completed = 0
    gantt_chart = []
    executed_processes = []
    ready_queue = []
    
    while completed < n:
        # Find processes that have arrived by the current time
        for i in range(n):
            if processes[i].arrival_time <= current_time and not processes[i].is_completed():
                if processes[i] not in ready_queue:
                    ready_queue.append(processes[i])
        
        if not ready_queue:
            # No process is ready, advance time
            current_time += 1
            continue
        
        # Sort ready queue by priority (lower number = higher priority)
        ready_queue.sort(key=lambda p: p.priority)
        
        # Get the highest priority process
        process = ready_queue.pop(0)
        
        # Set start time
        process.start_time = current_time
        
        # Execute the process (runs to completion in priority scheduling)
        execution_time = process.execute()
        
        # Update current time
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
        
        completed += 1
    
    return executed_processes, gantt_chart