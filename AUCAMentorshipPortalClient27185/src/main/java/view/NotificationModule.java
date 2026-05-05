package view;

import model.Notification;
import model.User;
import util.ServiceRegistry;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Message", "Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        notifTable = new JTable(tableModel);
        add(new JScrollPane(notifTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
        JButton markReadBtn = new JButton("Mark as Read");
        
        refreshBtn.addActionListener(e -> loadNotifications());
        markReadBtn.addActionListener(e -> markAsRead());
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(markReadBtn);
        
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
                        n.getMessage(), n.getCreatedAt(), n.isRead() ? "Read" : "New"
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
        
        // In a real app, you'd get the ID and update the status
        JOptionPane.showMessageDialog(this, "Notification marked as read!");
        loadNotifications();
    }
}
