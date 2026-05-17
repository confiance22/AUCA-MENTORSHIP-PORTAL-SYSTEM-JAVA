package view;

import model.MentorshipProgram;
import model.MentorshipSession;
import model.SessionStatus;
import model.Notification;
import model.NotificationType;
import model.User;
import model.UserRole;
import model.ProgramStatus;
import util.ServiceRegistry;
import util.TableStyleUtil;
import util.UITheme;
import util.DialogStyleUtil;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;

public class ProgramModule extends JPanel {
    private User currentUser;
    private JTable programTable;
    private DefaultTableModel tableModel;
    private List<MentorshipProgram> allPrograms;

    public ProgramModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setOpaque(false);

        // Title text based on role
        String title = "Mentorship Programs";
        if (currentUser.getRole() == UserRole.MENTEE) title = "Browse Mentorship Programs";
        else if (currentUser.getRole() == UserRole.MENTOR) title = "My Mentorship Programs";
        else if (currentUser.getRole() == UserRole.ADMIN) title = "All Mentorship Programs";

        // --- MODERN HEADER PANEL WITH INTEGRATED REAL-TIME SEARCH ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel leftHeaderPanel = new JPanel();
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.setLayout(new BoxLayout(leftHeaderPanel, BoxLayout.X_AXIS));

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        leftHeaderPanel.add(titleLabel);

        // Responsive Search Input Field (For Mentee/All Roles)
        leftHeaderPanel.add(Box.createRigidArea(new Dimension(25, 0)));
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 34));
        searchField.setMaximumSize(new Dimension(220, 34));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search by title or mentor...");
        searchField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchField.putClientProperty(FlatClientProperties.STYLE, 
            "background: #16171D; " +
            "foreground: #F3F4F6; " +
            "arc: 8; " +
            "borderWidth: 1; " +
            "borderColor: #262930; " +
            "focusColor: #2563EB; " +
            "focusedBorderColor: #2563EB"
        );
        JLabel searchIcon = new JLabel("  ⌕  "); // Universal 16-bit magnifying symbol
        searchIcon.setForeground(new Color(156, 163, 175));
        searchField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, searchIcon);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterPrograms(searchField.getText()); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterPrograms(searchField.getText()); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterPrograms(searchField.getText()); }
        });

        leftHeaderPanel.add(searchField);
        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);

        // Buttons panel in the top-right header area
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
        refreshBtn.addActionListener(e -> loadPrograms());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.MENTOR) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton createBtn = new JButton("+ Create Program");
            createBtn.setPreferredSize(new Dimension(140, 34));
            createBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            createBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #10B981; " +
                "foreground: #FFFFFF; " +
                "arc: 8; " +
                "hoverBackground: #059669; " +
                "focusedBackground: #10B981; " +
                "borderWidth: 0; " +
                "focusWidth: 0"
            );
            createBtn.addActionListener(e -> createProgram());
            buttonPanel.add(createBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton enrollBtn = new JButton("Enroll");
            enrollBtn.setPreferredSize(new Dimension(100, 34));
            enrollBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            enrollBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            enrollBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #10B981; " +
                "foreground: #FFFFFF; " +
                "arc: 8; " +
                "hoverBackground: #059669; " +
                "focusedBackground: #10B981; " +
                "borderWidth: 0; " +
                "focusWidth: 0"
            );
            enrollBtn.addActionListener(e -> enrollInProgram());
            buttonPanel.add(enrollBtn);
            
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton bookBtn = new JButton("Book Session");
            bookBtn.setPreferredSize(new Dimension(130, 34));
            bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bookBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #4F46E5; " +
                "foreground: #FFFFFF; " +
                "arc: 8; " +
                "hoverBackground: #4338CA; " +
                "focusedBackground: #4F46E5; " +
                "borderWidth: 0; " +
                "focusWidth: 0"
            );
            bookBtn.addActionListener(e -> bookSession());
            buttonPanel.add(bookBtn);
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton deleteBtn = new JButton("Delete Selected");
            deleteBtn.setPreferredSize(new Dimension(140, 34));
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
            deleteBtn.addActionListener(e -> deleteProgram());
            buttonPanel.add(deleteBtn);
        }
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- MODERN FLOATING TABLE PANEL ---
        String[] columnNames = {"ID", "Title", "Mentor", "Start Date", "End Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        programTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(programTable, currentUser.getRole());
        
        JScrollPane scrollPane = new JScrollPane(programTable);
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);

        loadPrograms();
    }

    private void loadPrograms() {
        try {
            if (currentUser.getRole() == UserRole.MENTOR) {
                allPrograms = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordsByUserId(currentUser.getId());
            } else {
                allPrograms = ServiceRegistry.mentorshipProgramService.findAllMentorshipProgramRecords();
            }
            displayPrograms(allPrograms);
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error loading programs: " + ex.getMessage());
        }
    }

    private void displayPrograms(List<MentorshipProgram> programs) {
        tableModel.setRowCount(0);
        if (programs != null) {
            for (MentorshipProgram p : programs) {
                tableModel.addRow(new Object[]{
                    p.getId(), p.getTitle(), p.getCreatedBy() != null ? p.getCreatedBy().getFirstName() : "System",
                    p.getStartDate(), p.getEndDate(), p.getStatus()
                });
            }
        }
    }

    private void filterPrograms(String query) {
        if (allPrograms == null) return;
        if (query == null || query.trim().isEmpty()) {
            displayPrograms(allPrograms);
            return;
        }
        
        String lowerQuery = query.toLowerCase().trim();
        java.util.List<MentorshipProgram> filtered = new java.util.ArrayList<>();
        for (MentorshipProgram p : allPrograms) {
            String title = p.getTitle() != null ? p.getTitle().toLowerCase() : "";
            String mentorName = (p.getCreatedBy() != null && p.getCreatedBy().getFirstName() != null) ? p.getCreatedBy().getFirstName().toLowerCase() : "";
            
            if (title.contains(lowerQuery) || mentorName.contains(lowerQuery)) {
                filtered.add(p);
            }
        }
        displayPrograms(filtered);
    }

    private void createProgram() {
        JPanel panel = DialogStyleUtil.createStyledPanel();
        
        JTextField titleField = DialogStyleUtil.createStyledTextField("");
        JTextField descField = DialogStyleUtil.createStyledTextField("");
        JTextField capacityField = DialogStyleUtil.createStyledTextField("10");
        
        panel.add(DialogStyleUtil.createFieldLabel("Program Title:"));
        panel.add(titleField);
        panel.add(DialogStyleUtil.createFieldLabel("Description:"));
        panel.add(descField);
        panel.add(DialogStyleUtil.createFieldLabel("Max Capacity:"));
        panel.add(capacityField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Create New Mentorship Program", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String desc = descField.getText();
            String capStr = capacityField.getText();
            
            if (title.isEmpty()) {
                MessageDialogUtil.showWarning(this, "Title is required.");
                return;
            }

            try {
                MentorshipProgram program = new MentorshipProgram();
                program.setTitle(title);
                program.setDescription(desc.isEmpty() ? "No description provided." : desc);
                program.setMaxCapacity(Integer.parseInt(capStr));
                program.setCreatedBy(currentUser);
                program.setStartDate(LocalDate.now().plusDays(7));
                program.setEndDate(LocalDate.now().plusMonths(6));
                program.setStatus(ProgramStatus.ACTIVE);
                program.setCreatedAt(LocalDateTime.now());
                
                ServiceRegistry.mentorshipProgramService.registerMentorshipProgramRecord(program);
                
                ServiceRegistry.notificationService.notifyAdmins("New Mentorship Program Proposed: " + title + " (by Mentor " + currentUser.getFirstName() + ")");

                MessageDialogUtil.showSuccess(this, "Program '" + title + "' created successfully!");
                loadPrograms();
            } catch (Exception ex) {
                ex.printStackTrace();
                MessageDialogUtil.showError(this, "Error creating program: " + ex.getMessage());
            }
        }
    }

    private void enrollInProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a program to enroll.");
            return;
        }
        
        Long programId = (Long) tableModel.getValueAt(selectedRow, 0);
        String programTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = MessageDialogUtil.showConfirm(this, "Do you want to enroll in '" + programTitle + "'?", "Enrollment");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MentorshipProgram searchP = new MentorshipProgram();
                searchP.setId(programId);
                MentorshipProgram program = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordById(searchP);
                
                if (program != null) {
                    boolean alreadyEnrolled = false;
                    if (program.getEnrolledUsers() != null) {
                        for (User u : program.getEnrolledUsers()) {
                            if (u.getId().equals(currentUser.getId())) {
                                alreadyEnrolled = true;
                                break;
                            }
                        }
                    } else {
                        program.setEnrolledUsers(new java.util.ArrayList<>());
                    }
                    
                    if (alreadyEnrolled) {
                        MessageDialogUtil.showWarning(this, "You are already enrolled in this program.");
                    } else {
                        program.getEnrolledUsers().add(currentUser);
                        ServiceRegistry.mentorshipProgramService.updateMentorshipProgramRecord(program);
                        
                        Notification notif = new Notification();
                        notif.setUser(program.getCreatedBy());
                        notif.setMessage("Mentee " + currentUser.getFirstName() + " has enrolled in your program: " + program.getTitle());
                        notif.setType(NotificationType.GENERAL);
                        notif.setCreatedAt(LocalDateTime.now());
                        ServiceRegistry.notificationService.registerNotificationRecord(notif);

                        MessageDialogUtil.showSuccess(this, "Successfully enrolled in " + programTitle + "!");
                        loadPrograms();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                MessageDialogUtil.showError(this, "Error enrolling in program: " + ex.getMessage());
            }
        }
    }

    private void bookSession() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a program to book a session.");
            return;
        }

        Long programId = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipProgram searchP = new MentorshipProgram();
            searchP.setId(programId);
            MentorshipProgram program = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordById(searchP);

            if (program != null) {
                boolean isEnrolled = false;
                if (program.getEnrolledUsers() != null) {
                    for (User u : program.getEnrolledUsers()) {
                        if (u.getId().equals(currentUser.getId())) {
                            isEnrolled = true;
                            break;
                        }
                    }
                }

                if (!isEnrolled) {
                    MessageDialogUtil.showWarning(this, "You must enroll in the program before booking a session.");
                    return;
                }

                MentorshipSession session = new MentorshipSession();
                session.setMentor(program.getCreatedBy());
                session.setMentee(currentUser);
                session.setProgram(program);
                session.setScheduledAt(LocalDateTime.now().plusDays(2));
                session.setStatus(SessionStatus.SCHEDULED);
                session.setCreatedAt(LocalDateTime.now());
                session.setDurationMinutes(60);

                ServiceRegistry.mentorshipSessionService.registerMentorshipSessionRecord(session);

                Notification notif = new Notification();
                notif.setUser(program.getCreatedBy());
                notif.setMessage("Mentee " + currentUser.getFirstName() + " has booked a session for your program: " + program.getTitle());
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                ServiceRegistry.notificationService.registerNotificationRecord(notif);

                MessageDialogUtil.showSuccess(this, "Session booked successfully for " + session.getScheduledAt() + "!\nMentor " + program.getCreatedBy().getFirstName() + " has been notified.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialogUtil.showError(this, "Error booking session: " + ex.getMessage());
        }
    }

    private void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a program to delete.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipProgram p = new MentorshipProgram();
            p.setId(id);
            ServiceRegistry.mentorshipProgramService.deleteMentorshipProgramRecord(p);
            MessageDialogUtil.showSuccess(this, "Program deleted.");
            loadPrograms();
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error deleting program: " + ex.getMessage());
        }
    }
}
