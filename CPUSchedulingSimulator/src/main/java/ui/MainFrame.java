/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author vedan
 */


import algorithm.*;
import model.Process;
import util.SchedulingMetrics;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel buttonPanel;
    private JPanel resultPanel;
    private JPanel ganttChartPanel;
    private JPanel metricsPanel;
    
    private JTable processTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    
    private JButton addProcessButton;
    private JButton removeProcessButton;
    private JButton runFCFSButton;
    private JButton runSJFButton;
    private JButton runSRTFButton;
    private JButton runPriorityButton;
    private JButton runPreemtivePriorityButton;
    private JButton runRoundRobinButton;
    private JButton clearButton;
    
    private JTextField timeQuantumField;
    private JLabel timeQuantumLabel;
    
    private JTextArea metricsTextArea;
    private JScrollPane metricsScrollPane;
    
    private List<Process> processes;
    private int nextProcessId;
    
    public MainFrame() {
        setTitle("CPU Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        
        processes = new ArrayList<>();
        nextProcessId = 1;
        
        initComponents();
        layoutComponents();
        setupListeners();
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Process Input"));
        
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(new TitledBorder(new EtchedBorder(), "Algorithm Selection"));
        
        resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Simulation Results"));
        resultPanel.setPreferredSize(new Dimension(980, 350));
        
        ganttChartPanel = new JPanel(new BorderLayout());
        ganttChartPanel.setBorder(new TitledBorder("Gantt Chart"));
        
        metricsPanel = new JPanel(new BorderLayout());
        metricsPanel.setBorder(new TitledBorder("Performance Metrics"));
        
        // Initialize the process table with better styling
        String[] columnNames = {"ID", "Process Name", "Arrival Time", "Burst Time", "Priority"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : Object.class;
            }
        };
        
        processTable = new JTable(tableModel);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        processTable.setRowHeight(25);
        processTable.setFillsViewportHeight(true);
        processTable.setIntercellSpacing(new Dimension(5, 5));
        processTable.setShowGrid(true);
        processTable.setGridColor(Color.LIGHT_GRAY);
        
        tableScrollPane = new JScrollPane(processTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 150));
        
        // Initialize buttons with consistent styling
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);
        Color buttonColor = new Color(70, 130, 180);
        Color buttonTextColor = Color.WHITE;
        
        addProcessButton = createStyledButton("Add Process", buttonFont, buttonColor, buttonTextColor);
        removeProcessButton = createStyledButton("Remove Selected", buttonFont, buttonColor, buttonTextColor);
        
        Color algorithmButtonColor = new Color(34, 139, 34);
        runFCFSButton = createStyledButton("Run FCFS", buttonFont, algorithmButtonColor, buttonTextColor);
        runSJFButton = createStyledButton("Run SJF", buttonFont, algorithmButtonColor, buttonTextColor);
        runSRTFButton = createStyledButton("Run SRTF", buttonFont, algorithmButtonColor, buttonTextColor);
        runPriorityButton = createStyledButton("Run Priority", buttonFont, algorithmButtonColor, buttonTextColor);
        runPreemtivePriorityButton = createStyledButton("Run Preemptive Priority", buttonFont, algorithmButtonColor, buttonTextColor);
        runRoundRobinButton = createStyledButton("Run Round Robin", buttonFont, algorithmButtonColor, buttonTextColor);
        
        Color clearButtonColor = new Color(220, 20, 60);
        clearButton = createStyledButton("Clear Results", buttonFont, clearButtonColor, buttonTextColor);
        
        // Time quantum input with better styling
        timeQuantumLabel = new JLabel("Time Quantum:");
        timeQuantumLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        timeQuantumField = new JTextField("2", 3);
        timeQuantumField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeQuantumField.setHorizontalAlignment(JTextField.CENTER);
        
        // Metrics display with better styling
        metricsTextArea = new JTextArea();
        metricsTextArea.setEditable(false);
        metricsTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        metricsTextArea.setMargin(new Insets(5, 5, 5, 5));
        
        metricsScrollPane = new JScrollPane(metricsTextArea);
        metricsScrollPane.setPreferredSize(new Dimension(900, 150));
    }
    
   private JButton createStyledButton(String text, Font font, Color bgColor, Color textColor) {
    JButton button = new JButton(text);
    button.setFont(font);
    button.setBackground(bgColor);
    button.setForeground(textColor);

    button.setOpaque(true); // Important: Draw background
    button.setContentAreaFilled(true); // Fill the area
    button.setFocusPainted(false); // Remove focus rectangle
    button.setBorder(new CompoundBorder(
        new LineBorder(bgColor.darker(), 1),
        new EmptyBorder(5, 10, 5, 10)
    ));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Disable dynamic painting behavior (VERY important for Nimbus/FlatLaf themes)
    button.setUI(new javax.swing.plaf.basic.BasicButtonUI());

    return button;
}


    
    private void layoutComponents() {
        // Input panel (top section)
        JPanel tableControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tableControlPanel.add(addProcessButton);
        tableControlPanel.add(removeProcessButton);
        
        inputPanel.add(tableScrollPane, BorderLayout.CENTER);
        inputPanel.add(tableControlPanel, BorderLayout.SOUTH);
        
        // Button panel (middle section) - using GridBagLayout for better organization
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // First row of algorithm buttons
        gbc.gridy = 0;
        gbc.gridx = 0;
        buttonPanel.add(runFCFSButton, gbc);
        
        gbc.gridx = 1;
        buttonPanel.add(runSJFButton, gbc);
        
        gbc.gridx = 2;
        buttonPanel.add(runSRTFButton, gbc);
        
        // Second row of algorithm buttons
        gbc.gridy = 1;
        gbc.gridx = 0;
        buttonPanel.add(runPriorityButton, gbc);
        
        gbc.gridx = 1;
        buttonPanel.add(runPreemtivePriorityButton, gbc);
        
        gbc.gridx = 2;
        JPanel rrPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        rrPanel.add(timeQuantumLabel);
        rrPanel.add(timeQuantumField);
        rrPanel.add(runRoundRobinButton);
        buttonPanel.add(rrPanel, gbc);
        
        // Result panel (bottom section)
        JPanel resultHeaderPanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Simulation Results");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultHeaderPanel.add(resultLabel, BorderLayout.WEST);
        
        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearButtonPanel.add(clearButton);
        resultHeaderPanel.add(clearButtonPanel, BorderLayout.EAST);
        
        // Gantt chart with scroll pane
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);
        ganttScrollPane.setBorder(null);
        
        // Split pane to allow resizing between Gantt chart and metrics
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ganttScrollPane, metricsScrollPane);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerLocation(200);
        
        resultPanel.add(resultHeaderPanel, BorderLayout.NORTH);
        resultPanel.add(splitPane, BorderLayout.CENTER);
        
        // Main layout
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void setupListeners() {
        addProcessButton.addActionListener(e -> addNewProcess());
        
        removeProcessButton.addActionListener(e -> removeSelectedProcess());
        
        clearButton.addActionListener(e -> {
            ganttChartPanel.removeAll();
            metricsTextArea.setText("");
            ganttChartPanel.revalidate();
            ganttChartPanel.repaint();
            resultPanel.revalidate();
            resultPanel.repaint();
        });
        
        runFCFSButton.addActionListener(e -> runAlgorithm(new FCFSAlgorithm()));
        
        runSJFButton.addActionListener(e -> runAlgorithm(new SJFAlgorithm()));
        
        runSRTFButton.addActionListener(e -> runAlgorithm(new SRTFAlgorithm()));
        
        runPriorityButton.addActionListener(e -> runAlgorithm(new PriorityAlgorithm()));
        
        runPreemtivePriorityButton.addActionListener(e -> runAlgorithm(new PreemtivePriorityAlgorithm()));
        
        runRoundRobinButton.addActionListener(e -> {
            try {
                int timeQuantum = Integer.parseInt(timeQuantumField.getText());
                if (timeQuantum <= 0) {
                    JOptionPane.showMessageDialog(this, "Time quantum must be greater than 0", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                runAlgorithm(new RoundRobinAlgorithm(timeQuantum));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for time quantum", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void addNewProcess() {
        String processName = "P" + nextProcessId;
        Object[] rowData = {nextProcessId, processName, 0, 5, 1};
        tableModel.addRow(rowData);
        nextProcessId++;
        updateProcessesFromTable();
    }
    
    private void removeSelectedProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            updateProcessesFromTable();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a process to remove", "No Selection", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateProcessesFromTable() {
        processes.clear();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
            String name = (String) tableModel.getValueAt(i, 1);
            int arrivalTime = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            int burstTime = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
            int priority = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
            
            processes.add(new Process(id, name, arrivalTime, burstTime, priority));
        }
    }
    
    private void runAlgorithm(SchedulingAlgorithm algorithm) {
        updateProcessesFromTable();
        
        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add processes before running the algorithm", "No Processes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        List<Process> results = algorithm.schedule(processes);
        List<GanttChartEntry> ganttData = algorithm.getGanttChartData();
        
        displayResults(algorithm.getName(), results, ganttData);
    }
    
    private void displayResults(String algorithmName, List<Process> results, List<GanttChartEntry> ganttData) {
        ganttChartPanel.removeAll();
        metricsTextArea.setText("");
        
        // Create Gantt chart
        GanttChartComponent ganttChart = new GanttChartComponent(ganttData);
        JScrollPane ganttScrollPane = new JScrollPane(ganttChart);
        ganttChartPanel.add(ganttScrollPane, BorderLayout.CENTER);
        
        // Calculate and display metrics
        double avgWaitingTime = SchedulingMetrics.calculateAverageWaitingTime(results);
        double avgTurnaroundTime = SchedulingMetrics.calculateAverageTurnaroundTime(results);
        double throughput = SchedulingMetrics.calculateThroughput(results);
        
        StringBuilder metrics = new StringBuilder();
        metrics.append("Algorithm: ").append(algorithmName).append("\n\n");
        metrics.append("Process Details:\n");
        metrics.append(String.format("%-5s %-12s %-12s %-12s %-12s %-12s %-12s\n", 
                "ID", "Name", "Arrival", "Burst", "Completion", "Turnaround", "Waiting"));
        
        for (Process p : results) {
            metrics.append(String.format("%-5d %-12s %-12d %-12d %-12d %-12d %-12d\n", 
                    p.getId(), p.getName(), p.getArrivalTime(), p.getBurstTime(), 
                    p.getCompletionTime(), p.getTurnaroundTime(), p.getWaitingTime()));
        }
        
        metrics.append("\nPerformance Metrics:\n");
        metrics.append(String.format("Average Waiting Time: %.2f time units\n", avgWaitingTime));
        metrics.append(String.format("Average Turnaround Time: %.2f time units\n", avgTurnaroundTime));
        metrics.append(String.format("Throughput: %.4f processes/time unit\n", throughput));
        
        metricsTextArea.setText(metrics.toString());
        
        // Refresh UI
        ganttChartPanel.revalidate();
        ganttChartPanel.repaint();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Set nicer font rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}