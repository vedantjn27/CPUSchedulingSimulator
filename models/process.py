# models/process.py 

class Process:
    """Represents a process in the system."""
    
    def __init__(self, pid, arrival_time, burst_time, priority=0):
        """
        Initialize a new process.
        
        Args:
            pid (int): Process ID
            arrival_time (int): Time at which process arrives
            burst_time (int): CPU burst time required
            priority (int, optional): Priority value (lower number = higher priority)
        """
        self.pid = pid
        self.arrival_time = arrival_time
        self.burst_time = burst_time
        self.priority = priority
        
        # For tracking execution
        self.remaining_time = burst_time
        self.completion_time = 0
        self.waiting_time = 0
        self.turnaround_time = 0
        self.start_time = 0
        self.response_time = -1  # -1 indicates not yet started
        
    def __str__(self):
        return f"Process {self.pid} (Arrival: {self.arrival_time}, Burst: {self.burst_time}, Priority: {self.priority})"
    
    def is_completed(self):
        """Check if process has completed execution."""
        return self.remaining_time <= 0
    
    def calculate_times(self):
        """Calculate turnaround and waiting time."""
        self.turnaround_time = self.completion_time - self.arrival_time
        self.waiting_time = self.turnaround_time - self.burst_time
        
    def execute(self, time_quantum=None):
        """
        Execute the process for a given time quantum.
        
        Args:
            time_quantum (int, optional): Amount of time to execute. 
                                         If None, execute until completion.
        
        Returns:
            int: Amount of time actually executed
        """
        if self.response_time == -1:
            self.response_time = max(0, self.start_time - self.arrival_time)
            
        if time_quantum is None or time_quantum >= self.remaining_time:
            # Process will complete
            executed_time = self.remaining_time
            self.remaining_time = 0
        else:
            # Process will be preempted
            executed_time = time_quantum
            self.remaining_time -= time_quantum
            
        return executed_time