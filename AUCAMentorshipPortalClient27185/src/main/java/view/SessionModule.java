package view;

import model.MentorshipSession;
import model.MentorshipFeedback;
import model.SessionStatus;
import model.Notification;
import model.NotificationType;
import model.User;
import model.UserRole;
import util.ServiceRegistry;
import util.TableStyleUtil;
import util.ButtonStyleUtil;
import util.MessageDialogUtil;
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
        
        JLabel titleLabel = new JLabel("Mentorship Sessions", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"ID", "Mentor", "Mentee", "Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        sessionTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(sessionTable);
        add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JButton refreshBtn = new JButton("Refresh");
        ButtonStyleUtil.applyPrimaryStyle(refreshBtn);
        refreshBtn.addActionListener(e -> loadSessions());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.MENTOR) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton completeBtn = new JButton("Mark Completed");
            ButtonStyleUtil.applySuccessStyle(completeBtn);
            completeBtn.addActionListener(e -> completeSession());
            buttonPanel.add(completeBtn);

            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton cancelBtn = new JButton("Cancel Session");
            ButtonStyleUtil.applyDangerStyle(cancelBtn);
            cancelBtn.addActionListener(e -> cancelSession());
            buttonPanel.add(cancelBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton feedbackBtn = new JButton("Leave Feedback");
            ButtonStyleUtil.applySuccessStyle(feedbackBtn);
            feedbackBtn.addActionListener(e -> leaveFeedback());
            buttonPanel.add(feedbackBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);

        loadSessions();
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
            MessageDialogUtil.showError(this, "Error loading sessions: " + ex.getMessage());
        }
    }

    private void leaveFeedback() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a session.");
            return;
        }

        String status = tableModel.getValueAt(selectedRow, 4).toString();
        if (!status.equals("COMPLETED")) {
            MessageDialogUtil.showWarning(this, "You can only leave feedback for COMPLETED sessions.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipSession searchS = new MentorshipSession();
            searchS.setId(id);
            MentorshipSession session = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordById(searchS);
            
            if (session != null) {
                MentorshipFeedback existing = ServiceRegistry.mentorshipFeedbackService.findFeedbackRecordBySessionId(id);
                if (existing != null) {
                    MessageDialogUtil.showWarning(this, "You have already left feedback for this session.");
                    return;
                }

                String ratingStr = MessageDialogUtil.showInput(this, "Enter Rating (1-5):", "Session Feedback");
                if (ratingStr == null) return;
                int rating = Integer.parseInt(ratingStr);
                
                String comment = MessageDialogUtil.showInput(this, "Enter Comment:", "Session Feedback");
                if (comment == null) return;

                MentorshipFeedback feedback = new MentorshipFeedback();
                feedback.setSession(session);
                feedback.setRating(rating);
                feedback.setComment(comment);
                feedback.setCreatedAt(LocalDateTime.now());

                ServiceRegistry.mentorshipFeedbackService.registerFeedbackRecord(feedback);
                MessageDialogUtil.showSuccess(this, "Thank you for your feedback!");
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error: " + ex.getMessage());
        }
    }

    private void completeSession() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a session.");
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
                
                Notification notif = new Notification();
                notif.setUser(session.getMentee());
                notif.setMessage("Your session with " + currentUser.getFirstName() + " has been marked as COMPLETED.");
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                ServiceRegistry.notificationService.registerNotificationRecord(notif);
                
                MessageDialogUtil.showSuccess(this, "Session marked as COMPLETED! Mentee notified.");
                loadSessions();
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error updating session: " + ex.getMessage());
        }
    }

    private void cancelSession() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialogUtil.showWarning(this, "Please select a session.");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            MentorshipSession searchS = new MentorshipSession();
            searchS.setId(id);
            MentorshipSession session = ServiceRegistry.mentorshipSessionService.findMentorshipSessionRecordById(searchS);
            
            if (session != null) {
                if (session.getStatus() != SessionStatus.SCHEDULED) {
                    MessageDialogUtil.showWarning(this, "Only SCHEDULED sessions can be cancelled.");
                    return;
                }

                session.setStatus(SessionStatus.CANCELLED);
                ServiceRegistry.mentorshipSessionService.updateMentorshipSessionRecord(session);
                
                Notification notif = new Notification();
                notif.setUser(session.getMentee());
                notif.setMessage("Your session with " + currentUser.getFirstName() + " has been CANCELLED by the mentor.");
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                ServiceRegistry.notificationService.registerNotificationRecord(notif);
                
                MessageDialogUtil.showSuccess(this, "Session CANCELLED! Mentee notified.");
                loadSessions();
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Error cancelling session: " + ex.getMessage());
        }
    }
}
