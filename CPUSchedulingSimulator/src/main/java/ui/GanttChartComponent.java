/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author vedan
 */

import algorithm.GanttChartEntry;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class GanttChartComponent extends JPanel {
    private List<GanttChartEntry> entries;
    private final int barHeight = 40;
    private final int timeScale = 40; // pixels per time unit
    private final int padding = 20;
    private final Color[] processColors = {
        new Color(102, 194, 165), // teal
        new Color(252, 141, 98),  // orange
        new Color(141, 160, 203), // purple
        new Color(231, 138, 195), // pink
        new Color(166, 216, 84),  // lime
        new Color(255, 217, 47),  // yellow
        new Color(229, 196, 148), // tan
        new Color(179, 179, 179)  // gray
    };
    
    public GanttChartComponent(List<GanttChartEntry> entries) {
        this.entries = entries;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Calculate preferred size based on chart data
        int maxTime = findMaxTime();
        int width = padding * 2 + maxTime * timeScale;
        int height = padding * 3 + barHeight;
        setPreferredSize(new Dimension(Math.max(800, width), Math.max(150, height)));
    }
    
    private int findMaxTime() {
        if (entries == null || entries.isEmpty()) {
            return 10; // Default minimum width
        }
        
        int maxTime = 0;
        for (GanttChartEntry entry : entries) {
            if (entry.getEndTime() > maxTime) {
                maxTime = entry.getEndTime();
            }
        }
        return maxTime;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        System.out.println("Painting Gantt chart with " + (entries != null ? entries.size() : 0) + " entries");
        
        if (entries == null || entries.isEmpty()) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("No data to display", padding, barHeight);
            return;
        }
        
        // Find the end time of the last process
        int maxTime = findMaxTime();
        
        // Draw Gantt chart bars
        int y = padding;
        int barY = y + 20;
        
        // Draw time scale on top
        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, y, padding + maxTime * timeScale, y);
        
        for (int t = 0; t <= maxTime; t++) {
            int x = padding + t * timeScale;
            g2d.drawLine(x, y - 5, x, y);
            g2d.drawString(Integer.toString(t), x - 5, y - 7);
        }
        
        // Draw process bars
        for (int i = 0; i < entries.size(); i++) {
            GanttChartEntry entry = entries.get(i);
            int startX = padding + entry.getStartTime() * timeScale;
            int width = entry.getDuration() * timeScale;
            
            // Make sure width is at least 1 pixel
            if (width < 1) width = 1;
            
            // Choose color based on process ID
            Color processColor = processColors[entry.getProcessId() % processColors.length];
            g2d.setColor(processColor);
            g2d.fillRect(startX, barY, width, barHeight);
            
            g2d.setColor(Color.BLACK);
            g2d.drawRect(startX, barY, width, barHeight);
            
            // Draw process ID in the middle of the bar
            String label = entry.getProcessName();
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            
            if (textWidth < width - 4) { // Only draw text if it fits
                int textX = startX + (width - textWidth) / 2;
                int textY = barY + barHeight / 2 + fm.getAscent() / 2;
                g2d.drawString(label, textX, textY);
            }
            
            // Draw start and end times under the bar
            g2d.drawString(Integer.toString(entry.getStartTime()), startX, barY + barHeight + 15);
            if (width > 20) { // Only draw end time if there's enough space from start time
                g2d.drawString(Integer.toString(entry.getEndTime()), startX + width - 10, barY + barHeight + 15);
            }
        }
    }
}