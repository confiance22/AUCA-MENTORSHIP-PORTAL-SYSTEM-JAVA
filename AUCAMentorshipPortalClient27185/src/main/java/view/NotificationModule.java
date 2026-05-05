package view;

import model.Notification;
import model.User;
import util.ServiceRegistry;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import util.TableStyleUtil;
import java.util.List;

public class NotificationModule extends JPanel {
    private User currentUser;
    private JTable notifTable;
    private DefaultTableModel tableModel;

    public NotificationModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("My Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup - hidden ID column
        String[] columnNames = {"ID", "Message", "Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        notifTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(notifTable);
        
        // Hide ID column
        notifTable.removeColumn(notifTable.getColumnModel().getColumn(0));
        
        add(new JScrollPane(notifTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton markReadBtn = new JButton("Mark as Read");
        JButton markAllReadBtn = new JButton("Mark All Read");
        
        refreshBtn.addActionListener(e -> loadNotifications());
        markReadBtn.addActionListener(e -> markAsRead());
        markAllReadBtn.addActionListener(e -> markAllRead());
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(markReadBtn);
        buttonPanel.add(markAllReadBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);

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
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + ex.getMessage());
        }
    }

    private void markAsRead() {
        int selectedRow = notifTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification.");
            return;
        }
        
        try {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            ServiceRegistry.notificationService.markNotificationAsRead(id);
            loadNotifications();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void markAllRead() {
        try {
            ServiceRegistry.notificationService.markAllReadNotificationRecordsByUserId(currentUser.getId());
            loadNotifications();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
