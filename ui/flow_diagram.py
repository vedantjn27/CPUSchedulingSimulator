# Add a new file: ui/flow_diagram.py

from PyQt5.QtWidgets import QWidget, QVBoxLayout
from PyQt5.QtGui import QPainter, QPen, QBrush, QColor, QFont
from PyQt5.QtCore import Qt, QRectF, QPointF

class FlowDiagramWidget(QWidget): 
    """Widget for displaying algorithm flow diagrams."""
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.algorithm = None
        self.setMinimumHeight(300)
        
    def set_algorithm(self, algorithm):
        """Set the algorithm to display."""
        self.algorithm = algorithm
        self.update()
        
    def paintEvent(self, event):
        """Paint the flow diagram."""
        if not self.algorithm:
            return
            
        painter = QPainter(self)
        painter.setRenderHint(QPainter.Antialiasing)
        
        width = self.width()
        height = self.height()
        
        # Draw background
        painter.fillRect(self.rect(), QColor(240, 240, 240))
        
        # Set up pens and brushes
        box_pen = QPen(QColor(70, 70, 70))
        box_pen.setWidth(2)
        
        arrow_pen = QPen(QColor(50, 50, 50))
        arrow_pen.setWidth(2)
        
        box_brush = QBrush(QColor(230, 230, 255))
        
        # Set font
        font = QFont("Arial", 10)
        painter.setFont(font)
        
        # Draw flow diagram based on algorithm
        if self.algorithm == "FCFS":
            self.draw_fcfs_diagram(painter, width, height, box_pen, arrow_pen, box_brush)
        elif self.algorithm == "SJF":
            self.draw_sjf_diagram(painter, width, height, box_pen, arrow_pen, box_brush)
        elif self.algorithm == "Priority":
            self.draw_priority_diagram(painter, width, height, box_pen, arrow_pen, box_brush)
        elif self.algorithm == "Round Robin":
            self.draw_rr_diagram(painter, width, height, box_pen, arrow_pen, box_brush)
            
    def draw_box(self, painter, x, y, w, h, text, pen, brush):
        """Draw a box with text."""
        painter.setPen(pen)
        painter.setBrush(brush)
        painter.drawRect(x, y, w, h)
        
        # Draw text
        painter.drawText(QRectF(x, y, w, h), Qt.AlignCenter, text)
        
    def draw_arrow(self, painter, x1, y1, x2, y2, pen):
        """Draw an arrow from (x1,y1) to (x2,y2)."""
        painter.setPen(pen)
        painter.drawLine(x1, y1, x2, y2)
        
        # Draw arrowhead
        angle = 0.5  # Arrow head angle
        size = 10    # Arrow head size
        
        dx = x2 - x1
        dy = y2 - y1
        length = (dx**2 + dy**2)**0.5
        
        if length == 0:
            return
            
        dx /= length
        dy /= length
        
        # Calculate arrow head points
        p1x = x2 - size * (dx * (1 + angle) + dy * angle)
        p1y = y2 - size * (dy * (1 + angle) - dx * angle)
        p2x = x2 - size * (dx * (1 + angle) - dy * angle)
        p2y = y2 - size * (dy * (1 + angle) + dx * angle)
        
        # Draw arrow head
        points = [QPointF(x2, y2), QPointF(p1x, p1y), QPointF(p2x, p2y)]
        painter.setBrush(QBrush(pen.color()))
        painter.drawPolygon(points)
        
    def draw_fcfs_diagram(self, painter, width, height, box_pen, arrow_pen, box_brush):
        """Draw FCFS flow diagram."""
        # Boxes
        box_width = 120
        box_height = 60
        start_x = width / 2 - box_width / 2
        y_spacing = height / 6
        
        # Start box
        self.draw_box(painter, start_x, y_spacing, box_width, box_height, "Start", box_pen, box_brush)
        
        # Sort by arrival time box
        self.draw_box(painter, start_x, 2 * y_spacing, box_width, box_height, "Sort by Arrival Time", box_pen, box_brush)
        
        # Execute in order box
        self.draw_box(painter, start_x, 3 * y_spacing, box_width, box_height, "Execute in Order", box_pen, box_brush)
        
        # End box
        self.draw_box(painter, start_x, 4 * y_spacing, box_width, box_height, "End", box_pen, box_brush)
        
        # Arrows
        center_x = start_x + box_width / 2
        self.draw_arrow(painter, center_x, y_spacing + box_height, center_x, 2 * y_spacing, arrow_pen)
        self.draw_arrow(painter, center_x, 2 * y_spacing + box_height, center_x, 3 * y_spacing, arrow_pen)
        self.draw_arrow(painter, center_x, 3 * y_spacing + box_height, center_x, 4 * y_spacing, arrow_pen)