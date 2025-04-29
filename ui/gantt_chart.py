# ui/gantt_chart.py

import matplotlib.pyplot as plt
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from PyQt5.QtWidgets import QWidget, QVBoxLayout

class GanttChartWidget(QWidget):
    """Widget for displaying Gantt charts."""
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.figure = plt.figure(figsize=(10, 4))
        self.canvas = FigureCanvas(self.figure)
        
        layout = QVBoxLayout()
        layout.addWidget(self.canvas)
        self.setLayout(layout)
    
    def plot_gantt_chart(self, gantt_data, algorithm_name):
        """
        Plot the Gantt chart for process execution.
        
        Args:
            gantt_data (list): List of dictionaries with 'pid', 'start', and 'end' keys
            algorithm_name (str): Name of the scheduling algorithm
        """
        self.figure.clear()
        ax = self.figure.add_subplot(111)
        
        # Set up the plot
        ax.set_title(f'Gantt Chart - {algorithm_name}')
        ax.set_xlabel('Time')
        ax.set_ylabel('Process')
        
        # Prepare data for plotting
        processes = []
        start_times = []
        durations = []
        colors = []
        
        # Color map for processes
        color_map = plt.cm.get_cmap('tab10')
        
        # Process the Gantt data
        for entry in gantt_data:
            pid = entry['pid']
            start = entry['start']
            end = entry['end']
            
            processes.append(f'P{pid}')
            start_times.append(start)
            durations.append(end - start)
            colors.append(color_map(pid % 10))
        
        # Plot horizontal bars for each process execution
        y_positions = range(len(processes))
        ax.barh(y_positions, durations, left=start_times, height=0.5, 
                color=colors, edgecolor='black', alpha=0.8)
        
        # Add text labels
        for i, (process, start, duration) in enumerate(zip(processes, start_times, durations)):
            if duration > 1:  # Only add text if there's enough space
                ax.text(start + duration/2, i, f'P{process[-1]}', 
                       ha='center', va='center', color='black', fontweight='bold')
        
        # Set y-axis tick labels
        unique_processes = sorted(list(set(processes)))
        ax.set_yticks(range(len(processes)))
        
        # Make y-ticks empty as we'll have redundant process labels
        ax.set_yticklabels(['' for _ in range(len(processes))])
        
        # Add grid
        ax.grid(True, axis='x', linestyle='--', alpha=0.7)
        
        # Add time markers
        max_time = max([entry['end'] for entry in gantt_data])
        ax.set_xticks(range(0, max_time + 1))
        
        # Add a legend
        handles = [plt.Rectangle((0,0),1,1, color=color_map(i % 10)) 
                  for i in range(len(unique_processes))]
        ax.legend(handles, unique_processes, loc='upper right')
        
        # Refresh the canvas
        self.canvas.draw()