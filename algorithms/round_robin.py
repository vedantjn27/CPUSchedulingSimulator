# algorithms/round_robin.py

def round_robin(processes, time_quantum):
    """
    Round Robin scheduling algorithm.
    
    Args:
        processes (list): List of Process objects
        time_quantum (int): Time quantum for Round Robin
        
    Returns:
        tuple: (executed_processes, gantt_chart_data)
    """
    # Create a deep copy of processes to avoid modifying the original
    import copy
    processes = sorted(copy.deepcopy(processes), key=lambda p: p.arrival_time)
    n = len(processes)
    
    # Initialize variables
    current_time = 0
    completed = 0
    gantt_chart = []
    executed_processes = []
    queue = []
    
    # Process tracking for the original processes
    process_map = {p.pid: p for p in processes}
    
    while completed < n:
        # Add newly arrived processes to the queue
        for p in processes:
            if p.arrival_time <= current_time and p.remaining_time > 0 and p not in queue:
                queue.append(p)
        
        if not queue:
            # No process is ready, advance time
            current_time += 1
            continue
        
        # Get the next process from the queue
        process = queue.pop(0)
        
        # Set start time if this is the first time the process is running
        if process.response_time == -1:
            process.start_time = current_time
        
        # Execute the process for the time quantum
        start_exec = current_time
        executed_time = process.execute(time_quantum)
        current_time += executed_time
        
        # Add to Gantt chart data
        gantt_chart.append({
            'pid': process.pid,
            'start': start_exec,
            'end': current_time
        })
        
        # If the process is completed
        if process.is_completed():
            process.completion_time = current_time
            process.calculate_times()
            executed_processes.append(process)
            completed += 1
        else:
            # Put the process back in the queue if it's not completed
            queue.append(process)
    
    # Make sure we return the updated processes with correct stats
    return executed_processes, gantt_chart