package service.impl;

import dao.MentorshipFeedbackDao;
import dao.impl.MentorshipFeedbackDaoImpl;
import dao.NotificationDao;
import dao.impl.NotificationDaoImpl;
import model.*;
import service.MentorshipFeedbackService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class MentorshipFeedbackServiceImpl extends UnicastRemoteObject implements MentorshipFeedbackService {

    private final MentorshipFeedbackDao feedbackDao;
    private final NotificationDao notificationDao;

    public MentorshipFeedbackServiceImpl() throws RemoteException {
        super();
        this.feedbackDao = new MentorshipFeedbackDaoImpl();
        this.notificationDao = new NotificationDaoImpl();
    }

    @Override
    public MentorshipFeedback registerFeedbackRecord(MentorshipFeedback feedback) throws RemoteException {
        feedbackDao.save(feedback);
        
        // Notify Mentor
        try {
            MentorshipSession session = feedback.getSession();
            if (session != null && session.getMentor() != null) {
                Notification notif = new Notification();
                notif.setUser(session.getMentor());
                notif.setMessage("You received new feedback from " + session.getMentee().getFirstName());
                notif.setType(NotificationType.GENERAL);
                notif.setCreatedAt(LocalDateTime.now());
                notificationDao.save(notif);
            }
        } catch (Exception e) {
            System.err.println("Failed to send feedback notification: " + e.getMessage());
        }

        return cleanFeedback(feedback);
    }

    @Override
    public MentorshipFeedback updateFeedbackRecord(MentorshipFeedback feedback) throws RemoteException {
        feedbackDao.update(feedback);
        return cleanFeedback(feedback);
    }

    @Override
    public MentorshipFeedback deleteFeedbackRecord(MentorshipFeedback feedback) throws RemoteException {
        feedbackDao.delete(feedback.getId());
        return cleanFeedback(feedback);
    }

    @Override
    public MentorshipFeedback findFeedbackRecordById(Long id) throws RemoteException {
        return cleanFeedback(feedbackDao.findById(id));
    }

    @Override
    public List<MentorshipFeedback> findAllFeedbackRecords() throws RemoteException {
        List<MentorshipFeedback> feedbacks = feedbackDao.findAll();
        if (feedbacks != null) feedbacks.forEach(this::cleanFeedback);
        return feedbacks;
    }

    @Override
    public MentorshipFeedback findFeedbackRecordBySessionId(Long sessionId) throws RemoteException {
        return cleanFeedback(feedbackDao.findBySessionId(sessionId));
    }

    @Override
    public List<MentorshipFeedback> findFeedbackRecordsByMentorId(Long mentorId) throws RemoteException {
        List<MentorshipFeedback> feedbacks = feedbackDao.findByMentorId(mentorId);
        if (feedbacks != null) feedbacks.forEach(this::cleanFeedback);
        return feedbacks;
    }

    private MentorshipFeedback cleanFeedback(MentorshipFeedback f) {
        if (f == null) return null;
        if (f.getSession() != null) {
            MentorshipSession s = f.getSession();
            if (s.getMentor() != null) cleanUser(s.getMentor());
            if (s.getMentee() != null) cleanUser(s.getMentee());
            if (s.getProgram() != null) {
                s.getProgram().setEnrolledUsers(null);
                if (s.getProgram().getCreatedBy() != null) cleanUser(s.getProgram().getCreatedBy());
            }
        }
        return f;
    }

    private void cleanUser(User user) {
        user.setSessionsAsMentor(null);
        user.setSessionsAsMentee(null);
        user.setNotifications(null);
        user.setMentorProfile(null);
    }
}
