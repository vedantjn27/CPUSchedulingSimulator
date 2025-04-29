# ui/main_window.py

from PyQt5.QtWidgets import (QMainWindow, QWidget, QVBoxLayout, QHBoxLayout, 
                             QTableWidget, QTableWidgetItem, QLabel, QPushButton, 
                             QComboBox, QSpinBox, QFormLayout, QTabWidget,
                             QGroupBox, QScrollArea, QMessageBox)
from PyQt5.QtCore import Qt
import sys
import numpy as np

from ui.gantt_chart import GanttChartWidget
from models.process import Process

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        
        self.setWindowTitle("CPU Scheduling Simulator")
        self.setGeometry(100, 100, 1200, 800)
        
        # List to store processes
        self.processes = []
        
        # Set up the main widget and layout
        self.central_widget = QWidget()
        self.setCentralWidget(self.central_widget)
        
        self.main_layout = QVBoxLayout(self.central_widget)
        
        # Create tab widget
        self.tab_widget = QTabWidget()
        self.main_layout.addWidget(self.tab_widget)
        
        # Create tabs
        self.create_input_tab()
        self.create_results_tab()
        
    def create_input_tab(self):
        """Create the input tab for process data entry."""
        input_tab = QWidget()
        input_layout = QVBoxLayout(input_tab)
        
        # Process input form
        input_group = QGroupBox("Add Process")
        form_layout = QFormLayout()
        
        self.arrival_input = QSpinBox()
        self.arrival_input.setRange(0, 999)
        form_layout.addRow("Arrival Time:", self.arrival_input)
        
        self.burst_input = QSpinBox()
        self.burst_input.setRange(1, 999)
        self.burst_input.setValue(1)
        form_layout.addRow("Burst Time:", self.burst_input)
        
        self.priority_input = QSpinBox()
        self.priority_input.setRange(1, 99)
        form_layout.addRow("Priority (lower = higher priority):", self.priority_input)
        
        add_button = QPushButton("Add Process")
        add_button.clicked.connect(self.add_process)
        form_layout.addRow("", add_button)
        
        input_group.setLayout(form_layout)
        input_layout.addWidget(input_group)
        
        # Process table
        self.process_table = QTableWidget()
        self.process_table.setColumnCount(4)
        self.process_table.setHorizontalHeaderLabels(["PID", "Arrival Time", "Burst Time", "Priority"])
        self.process_table.setEditTriggers(QTableWidget.NoEditTriggers)
        
        input_layout.addWidget(QLabel("Process List:"))
        input_layout.addWidget(self.process_table)
        
        # Buttons for managing processes
        button_layout = QHBoxLayout()
        
        clear_button = QPushButton("Clear All")
        clear_button.clicked.connect(self.clear_processes)
        button_layout.addWidget(clear_button)
        
        delete_button = QPushButton("Delete Selected")
        delete_button.clicked.connect(self.delete_selected_process)
        button_layout.addWidget(delete_button)
        
        input_layout.addLayout(button_layout)
        
        # Algorithm selection
        algo_group = QGroupBox("Algorithm Selection")
        algo_layout = QFormLayout()
        
        self.algo_combo = QComboBox()
        self.algo_combo.addItems(["First-Come-First-Served (FCFS)", 
                                  "Shortest Job First (SJF)",
                                  "Priority Scheduling",
                                  "Round Robin"])
        algo_layout.addRow("Algorithm:", self.algo_combo)
        
        self.time_quantum = QSpinBox()
        self.time_quantum.setRange(1, 100)
        self.time_quantum.setValue(2)
        algo_layout.addRow("Time Quantum (for Round Robin):", self.time_quantum)
        
        self.algo_combo.currentIndexChanged.connect(self.update_time_quantum_visibility)
        
        # Initially hide time quantum
        self.update_time_quantum_visibility(0)
        
        algo_group.setLayout(algo_layout)
        input_layout.addWidget(algo_group)
        
        # Run button
        run_button = QPushButton("Run Simulation")
        run_button.clicked.connect(self.run_simulation)
        input_layout.addWidget(run_button)
        
        # Add random processes button
        random_button = QPushButton("Generate Random Processes")
        random_button.clicked.connect(self.generate_random_processes)
        input_layout.addWidget(random_button)
        
        self.tab_widget.addTab(input_tab, "Input")
        
    def create_results_tab(self):
        """Create the results tab for displaying simulation results."""
        results_tab = QWidget()
        results_layout = QVBoxLayout(results_tab)
        
        # Gantt chart
        gantt_group = QGroupBox("Gantt Chart")
        gantt_layout = QVBoxLayout()
        self.gantt_widget = GanttChartWidget()
        gantt_layout.addWidget(self.gantt_widget)
        gantt_group.setLayout(gantt_layout)
        results_layout.addWidget(gantt_group)
        
        # Results table
        results_group = QGroupBox("Process Statistics")
        results_layout_group = QVBoxLayout()
        
        self.results_table = QTableWidget()
        self.results_table.setColumnCount(6)
        self.results_table.setHorizontalHeaderLabels([
            "PID", "Arrival Time", "Burst Time", 
            "Completion Time", "Turnaround Time", "Waiting Time"
        ])
        self.results_table.setEditTriggers(QTableWidget.NoEditTriggers)
        
        results_layout_group.addWidget(self.results_table)
        results_group.setLayout(results_layout_group)
        results_layout.addWidget(results_group)
        
        # Summary statistics
        summary_group = QGroupBox("Summary")
        summary_layout = QFormLayout()
        
        self.avg_turnaround = QLabel("N/A")
        self.avg_waiting = QLabel("N/A")
        self.throughput = QLabel("N/A")
        
        summary_layout.addRow("Average Turnaround Time:", self.avg_turnaround)
        summary_layout.addRow("Average Waiting Time:", self.avg_waiting)
        summary_layout.addRow("Throughput (processes/unit time):", self.throughput)
        
        summary_group.setLayout(summary_layout)
        results_layout.addWidget(summary_group)
        
        self.tab_widget.addTab(results_tab, "Results")
        
    def update_time_quantum_visibility(self, index):
        """Show/hide time quantum based on selected algorithm."""
        # Show time quantum only for Round Robin
        if index == 3:  # Round Robin
            self.time_quantum.setEnabled(True)
        else:
            self.time_quantum.setEnabled(False)
            
    def add_process(self):
        """Add a new process to the list."""
        arrival_time = self.arrival_input.value()
        burst_time = self.burst_input.value()
        priority = self.priority_input.value()
        
        # Create a new process with PID = current number of processes + 1
        pid = len(self.processes) + 1
        process = Process(pid, arrival_time, burst_time, priority)
        
        # Add to the list
        self.processes.append(process)
        
        # Update the table
        self.update_process_table()
        
        # Reset input fields
        self.arrival_input.setValue(0)
        self.burst_input.setValue(1)
        self.priority_input.setValue(1)
        
    def update_process_table(self):
        """Update the process table with current processes."""
        self.process_table.setRowCount(len(self.processes))
        
        for i, process in enumerate(self.processes):
            self.process_table.setItem(i, 0, QTableWidgetItem(str(process.pid)))
            self.process_table.setItem(i, 1, QTableWidgetItem(str(process.arrival_time)))
            self.process_table.setItem(i, 2, QTableWidgetItem(str(process.burst_time)))
            self.process_table.setItem(i, 3, QTableWidgetItem(str(process.priority)))
            
    def clear_processes(self):
        """Clear all processes."""
        self.processes = []
        self.update_process_table()
        
    def delete_selected_process(self):
        """Delete the selected process."""
        selected_items = self.process_table.selectedItems()
        if not selected_items:
            return
            
        # Get the row index of the first selected item
        row = selected_items[0].row()
        
        # Remove the process from the list
        if 0 <= row < len(self.processes):
            self.processes.pop(row)
            
            # Reassign PIDs to ensure they're sequential
            for i, process in enumerate(self.processes):
                process.pid = i + 1
                
            # Update the table
            self.update_process_table()
            
    def generate_random_processes(self):
        """Generate random processes for testing."""
        # Clear existing processes
        self.processes = []
        
        # Generate 5 random processes
        for i in range(5):
            arrival_time = np.random.randint(0, 10)
            burst_time = np.random.randint(1, 10)
            priority = np.random.randint(1, 10)
            
            process = Process(i + 1, arrival_time, burst_time, priority)
            self.processes.append(process)
            
        # Update the table
        self.update_process_table()
        
    def run_simulation(self):
        """Run the selected scheduling algorithm."""
        if not self.processes:
            QMessageBox.warning(self, "Warning", "Please add at least one process.")
            return
            
        # Import the algorithms dynamically
        from algorithms.fcfs import fcfs
        from algorithms.sjf import sjf
        from algorithms.priority import priority_scheduling
        from algorithms.round_robin import round_robin
        
        # Get the selected algorithm
        algo_index = self.algo_combo.currentIndex()
        
        # Create a deep copy of processes to avoid modifying originals
        import copy
        processes_copy = copy.deepcopy(self.processes)
        
        # Run the selected algorithm
        if algo_index == 0:  # FCFS
            executed_processes, gantt_data = fcfs(processes_copy)
            algo_name = "First-Come-First-Served"
        elif algo_index == 1:  # SJF
            executed_processes, gantt_data = sjf(processes_copy)
            algo_name = "Shortest Job First"
        elif algo_index == 2:  # Priority
            executed_processes, gantt_data = priority_scheduling(processes_copy)
            algo_name = "Priority Scheduling"
        elif algo_index == 3:  # Round Robin
            time_quantum = self.time_quantum.value()
            executed_processes, gantt_data = round_robin(processes_copy, time_quantum)
            algo_name = f"Round Robin (Time Quantum: {time_quantum})"
        
        # Display the results
        self.display_results(executed_processes, gantt_data, algo_name)
        
        # Switch to the results tab
        self.tab_widget.setCurrentIndex(1)
        
    def display_results(self, executed_processes, gantt_data, algo_name):
        """Display the simulation results."""
        # Plot the Gantt chart
        self.gantt_widget.plot_gantt_chart(gantt_data, algo_name)
        
        # Update the results table
        self.results_table.setRowCount(len(executed_processes))
        
        total_turnaround = 0
        total_waiting = 0
        max_completion = 0
        
        for i, process in enumerate(executed_processes):
            self.results_table.setItem(i, 0, QTableWidgetItem(str(process.pid)))
            self.results_table.setItem(i, 1, QTableWidgetItem(str(process.arrival_time)))
            self.results_table.setItem(i, 2, QTableWidgetItem(str(process.burst_time)))
            self.results_table.setItem(i, 3, QTableWidgetItem(str(process.completion_time)))
            self.results_table.setItem(i, 4, QTableWidgetItem(str(process.turnaround_time)))
            self.results_table.setItem(i, 5, QTableWidgetItem(str(process.waiting_time)))
            
            total_turnaround += process.turnaround_time
            total_waiting += process.waiting_time
            max_completion = max(max_completion, process.completion_time)
            
        # Calculate and display summary statistics
        avg_turnaround = total_turnaround / len(executed_processes)
        avg_waiting = total_waiting / len(executed_processes)
        throughput = len(executed_processes) / max_completion if max_completion > 0 else 0
        
        self.avg_turnaround.setText(f"{avg_turnaround:.2f}")
        self.avg_waiting.setText(f"{avg_waiting:.2f}")
        self.throughput.setText(f"{throughput:.4f}")