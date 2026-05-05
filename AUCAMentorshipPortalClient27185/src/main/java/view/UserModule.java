package view;

import model.User;
import model.UserRole;
import util.ServiceRegistry;
import util.TableStyleUtil;
import util.ButtonStyleUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserModule extends JPanel {
    private User currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("User Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Role", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(userTable);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JButton refreshBtn = new JButton("Refresh List");
        ButtonStyleUtil.applyPrimaryStyle(refreshBtn);
        refreshBtn.addActionListener(e -> loadUsers());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.ADMIN) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton deleteBtn = new JButton("Delete User");
            ButtonStyleUtil.applyDangerStyle(deleteBtn);
            deleteBtn.addActionListener(e -> deleteSelectedUser());
            buttonPanel.add(deleteBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);

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
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                User userToDelete = new User();
                userToDelete.setId(id);
                ServiceRegistry.userService.deleteUserRecord(userToDelete);
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                loadUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage());
            }
        }
    }
}
