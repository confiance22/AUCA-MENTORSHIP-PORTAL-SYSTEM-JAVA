package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import model.UserRole;
import com.formdev.flatlaf.FlatClientProperties;

public class TableStyleUtil {

    public static void applyCustomStyle(JTable table, UserRole role) {
        Color themeColor = UITheme.getColorForRole(role);
        
        // Let the table breathe with extra spacing
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        
        // Soft, modern selection colors instead of harsh defaults
        Color softSelection = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 35);
        table.setSelectionBackground(softSelection);
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.setFocusable(false);
        
        // Fonts
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // 1. Header Overhaul (Capitalized, small font, spacious letter separation)
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(100, 42));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String headerText = (value != null) ? value.toString().toUpperCase() : "";
                
                // Add tiny spaces between letters for a tracking/letter-spacing effect
                StringBuilder spaced = new StringBuilder();
                for (int i = 0; i < headerText.length(); i++) {
                    spaced.append(headerText.charAt(i)).append(" ");
                }
                label.setText(spaced.toString().trim());
                label.setFont(new Font("Segoe UI", Font.BOLD, 10));
                label.setForeground(UITheme.TEXT_MUTED);
                label.setBackground(UITheme.BACKGROUND_MAIN);
                label.setHorizontalAlignment(JLabel.LEFT);
                
                // Fine matte bottom border line under the headers
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
                    BorderFactory.createEmptyBorder(0, 15, 0, 15)
                ));
                return label;
            }
        });

        // 2. Custom Row & Badge Renderer
        PremiumCellRenderer rowRenderer = new PremiumCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        
        // Apply FlatLaf rounded corner borders to the scroll pane wrapper
        Container parent = table.getParent();
        if (parent instanceof JViewport) {
            Container scrollPane = parent.getParent();
            if (scrollPane instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) scrollPane;
                sp.setBackground(UITheme.BACKGROUND_MAIN);
                sp.getViewport().setBackground(UITheme.BACKGROUND_MAIN);
                sp.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
                sp.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
            }
        }
    }

    private static class PremiumCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            // Faint divider lines & alternating rows
            if (!isSelected) {
                if (row % 2 == 0) {
                    label.setBackground(UITheme.BACKGROUND_CARD);
                } else {
                    label.setBackground(new Color(26, 27, 34)); // Faintly lighter obsidian row
                }
            } else {
                label.setBackground(table.getSelectionBackground());
            }

            label.setForeground(UITheme.TEXT_PRIMARY);
            label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

            // Status Badge Renderer Check
            String valStr = (value != null) ? value.toString() : "";
            if (valStr.equalsIgnoreCase("true") || valStr.equalsIgnoreCase("active") || valStr.equalsIgnoreCase("approved")) {
                label.setText("Active");
                label.setForeground(UITheme.ACCENT_EMERALD);
                putClientProperty("badgeType", "active");
            } else if (valStr.equalsIgnoreCase("false") || valStr.equalsIgnoreCase("inactive") || valStr.equalsIgnoreCase("blocked")) {
                label.setText("Inactive");
                label.setForeground(UITheme.ACCENT_CRIMSON);
                putClientProperty("badgeType", "inactive");
            } else if (valStr.equalsIgnoreCase("pending")) {
                label.setText("Pending");
                label.setForeground(UITheme.TEXT_MUTED);
                putClientProperty("badgeType", "pending");
            } else {
                putClientProperty("badgeType", null);
            }

            return label;
        }

        @Override
        protected void paintComponent(Graphics g) {
            String badge = (String) getClientProperty("badgeType");
            if (badge != null) {
                // High-fidelity custom painted pill badge
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                // Draw table row background
                g2.setColor(getBackground());
                g2.fillRect(0, 0, w, h);

                // Draw line separator at the bottom of cells
                g2.setColor(UITheme.BORDER_COLOR);
                g2.drawLine(0, h - 1, w, h - 1);

                // Badge measurements
                int badgeW = 75;
                int badgeH = 22;
                int badgeX = 15; // aligned with text padding
                int badgeY = (h - badgeH) / 2;

                if (badge.equals("active")) {
                    g2.setColor(new Color(16, 185, 129, 30)); // 12% Opacity Emerald Green background
                    g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(new Color(16, 185, 129, 80)); // Fine border line
                    g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(UITheme.ACCENT_EMERALD);
                } else if (badge.equals("inactive")) {
                    g2.setColor(new Color(239, 68, 68, 30)); // Crimson background
                    g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(new Color(239, 68, 68, 80));
                    g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(UITheme.ACCENT_CRIMSON);
                } else {
                    g2.setColor(new Color(156, 163, 175, 30)); // Muted pending background
                    g2.fillRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(new Color(156, 163, 175, 80));
                    g2.drawRoundRect(badgeX, badgeY, badgeW, badgeH, 12, 12);
                    g2.setColor(UITheme.TEXT_MUTED);
                }

                // Centered typography inside pill badge
                g2.setFont(getFont().deriveFont(Font.BOLD, 10f));
                FontMetrics fm = g2.getFontMetrics();
                String txt = getText();
                int tx = badgeX + (badgeW - fm.stringWidth(txt)) / 2;
                int ty = badgeY + (badgeH - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(txt, tx, ty);
                
                g2.dispose();
            } else {
                // Regular field rendering
                super.paintComponent(g);
                
                // Draw fine separator line
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BORDER_COLOR);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        }
    }
}
