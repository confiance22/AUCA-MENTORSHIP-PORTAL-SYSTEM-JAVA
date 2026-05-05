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
import util.ButtonStyleUtil;
import util.DialogStyleUtil;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class ProgramModule extends JPanel {
    private User currentUser;
    private JTable programTable;
    private DefaultTableModel tableModel;

    public ProgramModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        String title = "Mentorship Programs";
        if (currentUser.getRole() == UserRole.MENTEE) title = "Browse Mentorship Programs";
        else if (currentUser.getRole() == UserRole.MENTOR) title = "My Mentorship Programs";
        else if (currentUser.getRole() == UserRole.ADMIN) title = "All Mentorship Programs";

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Title", "Mentor", "Start Date", "End Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        programTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(programTable);
        add(new JScrollPane(programTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JButton refreshBtn = new JButton("Refresh");
        ButtonStyleUtil.applyPrimaryStyle(refreshBtn);
        refreshBtn.addActionListener(e -> loadPrograms());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.MENTOR) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton createBtn = new JButton("Create New Program");
            ButtonStyleUtil.applySuccessStyle(createBtn);
            createBtn.addActionListener(e -> createProgram());
            buttonPanel.add(createBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton enrollBtn = new JButton("Enroll in Program");
            ButtonStyleUtil.applySuccessStyle(enrollBtn);
            enrollBtn.addActionListener(e -> enrollInProgram());
            buttonPanel.add(enrollBtn);
            
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton bookBtn = new JButton("Book Session");
            ButtonStyleUtil.applyPrimaryStyle(bookBtn);
            bookBtn.addActionListener(e -> bookSession());
            buttonPanel.add(bookBtn);
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton deleteBtn = new JButton("Delete Selected");
            ButtonStyleUtil.applyDangerStyle(deleteBtn);
            deleteBtn.addActionListener(e -> deleteProgram());
            buttonPanel.add(deleteBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);

        loadPrograms();
    }

    private void loadPrograms() {
        try {
            List<MentorshipProgram> programs;
            if (currentUser.getRole() == UserRole.MENTOR) {
                programs = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordsByUserId(currentUser.getId());
            } else {
                programs = ServiceRegistry.mentorshipProgramService.findAllMentorshipProgramRecords();
            }
            
            tableModel.setRowCount(0);
            if (programs != null) {
                for (MentorshipProgram p : programs) {
                    tableModel.addRow(new Object[]{
                        p.getId(), p.getTitle(), p.getCreatedBy() != null ? p.getCreatedBy().getFirstName() : "System",
                        p.getStartDate(), p.getEndDate(), p.getStatus()
                    });
                }
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error loading programs: " + ex.getMessage());
        }
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
