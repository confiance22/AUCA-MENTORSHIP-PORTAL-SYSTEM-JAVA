package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TableStyleUtil {

    public static void applyCustomStyle(JTable table) {
        // Table general settings
        table.setRowHeight(35);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(new Color(239, 246, 255));
        table.setSelectionForeground(new Color(30, 58, 138));
        table.setFocusable(false);
        table.setRowMargin(5);
        
        // Font
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 40));
        header.setBackground(new Color(30, 58, 138)); // Deep Blue
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder());

        // Align text to left with padding
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
        
        // Remove border from scroll pane
        Container parent = table.getParent();
        if (parent instanceof JViewport) {
            Container scrollPane = parent.getParent();
            if (scrollPane instanceof JScrollPane) {
                ((JScrollPane) scrollPane).setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
                ((JScrollPane) scrollPane).getViewport().setBackground(Color.WHITE);
            }
        }
    }
}
