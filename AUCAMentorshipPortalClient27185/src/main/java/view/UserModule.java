package view;

import model.User;
import model.UserRole;
import util.ServiceRegistry;
import util.TableStyleUtil;
import util.UITheme;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;

public class UserModule extends JPanel {
    private User currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setOpaque(false); // Let the dashboard background shine through

        // --- MODERN HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("User Management", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Action buttons relocated to the top right header area
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
        refreshBtn.addActionListener(e -> loadUsers());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.ADMIN) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            
            JButton deleteBtn = new JButton("Delete User");
            deleteBtn.setPreferredSize(new Dimension(110, 34));
            deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Ghost red button that becomes solid crimson on hover
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
            deleteBtn.addActionListener(e -> deleteSelectedUser());
            buttonPanel.add(deleteBtn);
        }
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- MODERN FLOATING SCROLLPANE TABLE CONTAINER ---
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Role", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(userTable, currentUser.getRole());
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // Padded layout container wrapper for visual layout breathing room
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);

        loadUsers();
    }

    private void loadUsers() {
        try {
            List<User> users = ServiceRegistry.userService.findAllUserRecords();
            tableModel.setRowCount(0);
            if (users != null) {
                for (User u : users) {
                    tableModel.addRow(new Object[]{
                        u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getRole(), u.isIsActive()
                    });
                }
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a user to delete.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        int confirm = MessageDialogUtil.showConfirm(this, "Are you sure you want to delete user ID " + id + "?", "Confirm Delete");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                User userToDelete = new User();
                userToDelete.setId(id);
                ServiceRegistry.userService.deleteUserRecord(userToDelete);
                MessageDialogUtil.showSuccess(this, "User deleted successfully.");
                loadUsers();
            } catch (Exception ex) {
                MessageDialogUtil.showError(this, "Error deleting user: " + ex.getMessage());
            }
        }
    }
}
