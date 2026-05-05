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

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Title", "Mentor", "Start Date", "End Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        programTable = new JTable(tableModel);
        add(new JScrollPane(programTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(refreshBtn);
        
        refreshBtn.addActionListener(e -> loadPrograms());

        if (currentUser.getRole() == UserRole.MENTOR) {
            JButton createBtn = new JButton("Create New Program");
            createBtn.addActionListener(e -> createProgram());
            buttonPanel.add(createBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            JButton enrollBtn = new JButton("Enroll in Program");
            JButton bookBtn = new JButton("Book Session");
            enrollBtn.addActionListener(e -> enrollInProgram());
            bookBtn.addActionListener(e -> bookSession());
            buttonPanel.add(enrollBtn);
            buttonPanel.add(bookBtn);
        } else if (currentUser.getRole() == UserRole.ADMIN) {
            JButton deleteBtn = new JButton("Delete Selected");
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
            JOptionPane.showMessageDialog(this, "Error loading programs: " + ex.getMessage());
        }
    }

    private void createProgram() {
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField capacityField = new JTextField("10");
        
        Object[] message = {
            "Program Title:", titleField,
            "Description:", descField,
            "Max Capacity:", capacityField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create New Mentorship Program", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String desc = descField.getText();
            String capStr = capacityField.getText();
            
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title is required.");
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
                JOptionPane.showMessageDialog(this, "Program '" + title + "' created successfully!");
                loadPrograms();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating program: " + ex.getMessage());
            }
        }
    }

    private void enrollInProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a program to enroll.");
            return;
        }
        
        Long programId = (Long) tableModel.getValueAt(selectedRow, 0);
        String programTitle = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to enroll in '" + programTitle + "'?", "Enrollment", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Fetch the program object
                MentorshipProgram searchP = new MentorshipProgram();
                searchP.setId(programId);
                MentorshipProgram program = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordById(searchP);
                
                if (program != null) {
                    // Check if already enrolled
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
                        JOptionPane.showMessageDialog(this, "You are already enrolled in this program.");
                    } else {
                        program.getEnrolledUsers().add(currentUser);
                        ServiceRegistry.mentorshipProgramService.updateMentorshipProgramRecord(program);
                        
                        // Create Notification for Mentor
                        Notification notif = new Notification();
                        notif.setUser(program.getCreatedBy());
                        notif.setMessage("Mentee " + currentUser.getFirstName() + " has enrolled in your program: " + program.getTitle());
                        notif.setType(NotificationType.GENERAL);
                        notif.setCreatedAt(LocalDateTime.now());
                        ServiceRegistry.notificationService.registerNotificationRecord(notif);

                        JOptionPane.showMessageDialog(this, "Successfully enrolled in " + programTitle + "!");
                        loadPrograms();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error enrolling in program: " + ex.getMessage());
            }
        }
    }

    private void bookSession() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a program to book a session.");
            return;
        }

        Long programId = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipProgram searchP = new MentorshipProgram();
            searchP.setId(programId);
            MentorshipProgram program = ServiceRegistry.mentorshipProgramService.findMentorshipProgramRecordById(searchP);

            if (program != null) {
                // Check enrollment
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
                    JOptionPane.showMessageDialog(this, "You must enroll in the program before booking a session.");
                    return;
                }

                // Create Session
                MentorshipSession session = new MentorshipSession();
                session.setMentor(program.getCreatedBy());
                session.setMentee(currentUser);
                session.setProgram(program);
                session.setScheduledAt(LocalDateTime.now().plusDays(2));
                session.setStatus(SessionStatus.SCHEDULED);
                session.setCreatedAt(LocalDateTime.now());
                session.setDurationMinutes(60);

                ServiceRegistry.mentorshipSessionService.registerMentorshipSessionRecord(session);

                // Create Notification for Mentor
                Notification notif = new Notification();
                notif.setUser(program.getCreatedBy());
                notif.setMessage("Mentee " + currentUser.getFirstName() + " has booked a session for your program: " + program.getTitle());
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                ServiceRegistry.notificationService.registerNotificationRecord(notif);

                JOptionPane.showMessageDialog(this, "Session booked successfully for " + session.getScheduledAt() + "!\nMentor " + program.getCreatedBy().getFirstName() + " has been notified.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking session: " + ex.getMessage());
        }
    }

    private void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a program to delete.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipProgram p = new MentorshipProgram();
            p.setId(id);
            ServiceRegistry.mentorshipProgramService.deleteMentorshipProgramRecord(p);
            JOptionPane.showMessageDialog(this, "Program deleted.");
            loadPrograms();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting program: " + ex.getMessage());
        }
    }
}
