package view;

import model.Notification;
import model.User;
import util.ServiceRegistry;
import util.TableStyleUtil;
import util.UITheme;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;

public class NotificationModule extends JPanel {
    private User currentUser;
    private JTable notifTable;
    private DefaultTableModel tableModel;

    public NotificationModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- MODERN HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("My Notifications", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Relocated Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(100, 34));
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #16171D; " +
            "foreground: #9CA3AF; " +
            "arc: 8; " +
            "hoverBackground: #262930; " +
            "hoverForeground: #F3F4F6; " +
            "borderWidth: 1; " +
            "borderColor: #262930; " +
            "focusWidth: 0"
        );
        refreshBtn.addActionListener(e -> loadNotifications());
        buttonPanel.add(refreshBtn);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        JButton markReadBtn = new JButton("Mark Read");
        markReadBtn.setPreferredSize(new Dimension(110, 34));
        markReadBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markReadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markReadBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #10B981; " +
            "foreground: #FFFFFF; " +
            "arc: 8; " +
            "hoverBackground: #059669; " +
            "focusedBackground: #10B981; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        markReadBtn.addActionListener(e -> markAsRead());
        buttonPanel.add(markReadBtn);

        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        JButton markAllReadBtn = new JButton("Mark All Read");
        markAllReadBtn.setPreferredSize(new Dimension(130, 34));
        markAllReadBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markAllReadBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markAllReadBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #4F46E5; " +
            "foreground: #FFFFFF; " +
            "arc: 8; " +
            "hoverBackground: #4338CA; " +
            "focusedBackground: #4F46E5; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        markAllReadBtn.addActionListener(e -> markAllRead());
        buttonPanel.add(markAllReadBtn);

        // Crimson/Ghost Delete Selected button (Available for EVERY user)
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setPreferredSize(new Dimension(145, 34));
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #16171D; " +
            "foreground: #EF4444; " +
            "arc: 8; " +
            "hoverBackground: #EF4444; " +
            "hoverForeground: #FFFFFF; " +
            "borderWidth: 1; " +
            "borderColor: #EF4444; " +
            "focusWidth: 0"
        );
        deleteBtn.addActionListener(e -> deleteSelectedNotification());
        buttonPanel.add(deleteBtn);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- MODERN FLOATING TABLE PANEL ---
        String[] columnNames = {"ID", "Message", "Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        notifTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(notifTable, currentUser.getRole());
        
        // Hide ID column from View
        notifTable.removeColumn(notifTable.getColumnModel().getColumn(0));

        JScrollPane scrollPane = new JScrollPane(notifTable);
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);

        loadNotifications();
    }

    private void loadNotifications() {
        try {
            List<Notification> notifications = ServiceRegistry.notificationService.findNotificationRecordsByUserId(currentUser.getId());
            tableModel.setRowCount(0);
            if (notifications != null) {
                for (Notification n : notifications) {
                    tableModel.addRow(new Object[]{
                        n.getId(), n.getMessage(), n.getCreatedAt(), n.isRead() ? "Read" : "New"
                    });
                }
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error loading notifications: " + ex.getMessage());
        }
    }

    private void markAsRead() {
        int selectedRow = notifTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a notification.");
            return;
        }
        
        try {
            int modelRow = notifTable.convertRowIndexToModel(selectedRow);
            Long id = (Long) tableModel.getValueAt(modelRow, 0);
            ServiceRegistry.notificationService.markNotificationAsRead(id);
            loadNotifications();
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    private void markAllRead() {
        try {
            ServiceRegistry.notificationService.markAllReadNotificationRecordsByUserId(currentUser.getId());
            loadNotifications();
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelectedNotification() {
        int selectedRow = notifTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a notification to delete.");
            return;
        }

        int confirm = MessageDialogUtil.showConfirm(this, "Are you sure you want to delete this notification?", "Confirm Delete");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int modelRow = notifTable.convertRowIndexToModel(selectedRow);
                Long id = (Long) tableModel.getValueAt(modelRow, 0);
                Notification notif = new Notification();
                notif.setId(id);
                
                ServiceRegistry.notificationService.deleteNotificationRecord(notif);
                MessageDialogUtil.showSuccess(this, "Notification deleted successfully.");
                loadNotifications();
            } catch (Exception ex) {
                MessageDialogUtil.showError(this, "Error deleting notification: " + ex.getMessage());
            }
        }
    }
}
