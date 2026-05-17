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
import util.UITheme;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;

public class SessionModule extends JPanel {
    private User currentUser;
    private JTable sessionTable;
    private DefaultTableModel tableModel;

    public SessionModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setOpaque(false);

        // --- MODERN HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Mentorship Sessions", SwingConstants.LEFT);
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
        refreshBtn.addActionListener(e -> loadSessions());
        buttonPanel.add(refreshBtn);

        if (currentUser.getRole() == UserRole.MENTOR) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton completeBtn = new JButton("Complete");
            completeBtn.setPreferredSize(new Dimension(110, 34));
            completeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            completeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            completeBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #10B981; " +
                "foreground: #FFFFFF; " +
                "arc: 8; " +
                "hoverBackground: #059669; " +
                "focusedBackground: #10B981; " +
                "borderWidth: 0; " +
                "focusWidth: 0"
            );
            completeBtn.addActionListener(e -> completeSession());
            buttonPanel.add(completeBtn);

            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setPreferredSize(new Dimension(100, 34));
            cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #16171D; " +
                "foreground: #EF4444; " +
                "arc: 8; " +
                "hoverBackground: #EF4444; " +
                "hoverForeground: #FFFFFF; " +
                "borderWidth: 1; " +
                "borderColor: #EF4444; " +
                "focusWidth: 0"
            );
            cancelBtn.addActionListener(e -> cancelSession());
            buttonPanel.add(cancelBtn);
        } else if (currentUser.getRole() == UserRole.MENTEE) {
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            JButton feedbackBtn = new JButton("Leave Feedback");
            feedbackBtn.setPreferredSize(new Dimension(140, 34));
            feedbackBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            feedbackBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            feedbackBtn.putClientProperty(FlatClientProperties.STYLE, 
                "background: #4F46E5; " +
                "foreground: #FFFFFF; " +
                "arc: 8; " +
                "hoverBackground: #4338CA; " +
                "focusedBackground: #4F46E5; " +
                "borderWidth: 0; " +
                "focusWidth: 0"
            );
            feedbackBtn.addActionListener(e -> leaveFeedback());
            buttonPanel.add(feedbackBtn);
        }
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- MODERN FLOATING TABLE PANEL ---
        String[] columnNames = {"ID", "Mentor", "Mentee", "Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        sessionTable = new JTable(tableModel);
        TableStyleUtil.applyCustomStyle(sessionTable, currentUser.getRole());
        
        JScrollPane scrollPane = new JScrollPane(sessionTable);
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);

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
