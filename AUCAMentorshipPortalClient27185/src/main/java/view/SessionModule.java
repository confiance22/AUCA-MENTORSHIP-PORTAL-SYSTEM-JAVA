package view;

import model.MentorshipSession;
import model.MentorshipFeedback;
import model.SessionStatus;
import model.Notification;
import model.NotificationType;
import model.User;
import model.UserRole;
import util.ServiceRegistry;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class SessionModule extends JPanel {
    private User currentUser;
    private JTable sessionTable;
    private DefaultTableModel tableModel;

    public SessionModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Mentorship Sessions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Mentor", "Mentee", "Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        sessionTable = new JTable(tableModel);
        add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
        buttonPanel.add(refreshBtn);
        
        refreshBtn.addActionListener(e -> loadSessions());

        if (currentUser.getRole() == UserRole.MENTOR) {
            JButton completeBtn = new JButton("Mark Completed");
            completeBtn.addActionListener(e -> completeSession());
            buttonPanel.add(completeBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            JButton feedbackBtn = new JButton("Leave Feedback");
            feedbackBtn.addActionListener(e -> leaveFeedback());
            buttonPanel.add(feedbackBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);

        loadSessions();
    }

    private void leaveFeedback() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session.");
            return;
        }

        String status = (String) tableModel.getValueAt(selectedRow, 4).toString();
        if (!status.equals("COMPLETED")) {
            JOptionPane.showMessageDialog(this, "You can only leave feedback for COMPLETED sessions.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipSession searchS = new MentorshipSession();
            searchS.setId(id);
            MentorshipSession session = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordById(searchS);
            
            if (session != null) {
                // Check if feedback already exists
                MentorshipFeedback existing = ServiceRegistry.mentorshipFeedbackService.findFeedbackRecordBySessionId(id);
                if (existing != null) {
                    JOptionPane.showMessageDialog(this, "You have already left feedback for this session.");
                    return;
                }

                // Simple Input Dialogs for Feedback
                String ratingStr = JOptionPane.showInputDialog(this, "Enter Rating (1-5):");
                if (ratingStr == null) return;
                int rating = Integer.parseInt(ratingStr);
                
                String comment = JOptionPane.showInputDialog(this, "Enter Comment:");
                if (comment == null) return;

                MentorshipFeedback feedback = new MentorshipFeedback();
                feedback.setSession(session);
                feedback.setRating(rating);
                feedback.setComment(comment);
                feedback.setCreatedAt(LocalDateTime.now());

                ServiceRegistry.mentorshipFeedbackService.registerFeedbackRecord(feedback);
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void completeSession() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipSession searchS = new MentorshipSession();
            searchS.setId(id);
            MentorshipSession session = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordById(searchS);
            
            if (session != null) {
                session.setStatus(SessionStatus.COMPLETED);
                ServiceRegistry.mentorshipSessionService.updateMentorshipSessionRecord(session);
                
                // Notify Mentee
                Notification notif = new Notification();
                notif.setUser(session.getMentee());
                notif.setMessage("Your session with " + currentUser.getFirstName() + " has been marked as COMPLETED.");
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                ServiceRegistry.notificationService.registerNotificationRecord(notif);
                
                JOptionPane.showMessageDialog(this, "Session marked as COMPLETED! Mentee notified.");
                loadSessions();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating session: " + ex.getMessage());
        }
    }

    private void loadSessions() {
        try {
            List<MentorshipSession> sessions;
            if (currentUser.getRole() == UserRole.MENTOR) {
                sessions = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordsByMentorId(currentUser.getId());
            } else if (currentUser.getRole() == UserRole.MENTEE) {
                sessions = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordsByMenteeId(currentUser.getId());
            } else {
                sessions = ServiceRegistry.mentorshipSessionService.findAllMentorshipSessionRecords();
            }
            
            tableModel.setRowCount(0);
            if (sessions != null) {
                for (MentorshipSession s : sessions) {
                    tableModel.addRow(new Object[]{
                        s.getId(), 
                        s.getMentor() != null ? s.getMentor().getFirstName() : "N/A",
                        s.getMentee() != null ? s.getMentee().getFirstName() : "N/A",
                        s.getScheduledAt(), 
                        s.getStatus()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sessions: " + ex.getMessage());
        }
    }
}
