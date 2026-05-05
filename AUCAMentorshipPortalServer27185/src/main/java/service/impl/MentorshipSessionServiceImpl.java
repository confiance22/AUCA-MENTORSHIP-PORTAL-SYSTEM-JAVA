package service.impl;

import dao.MentorshipSessionDao;
import dao.impl.MentorshipSessionDaoImpl;
import model.MentorshipSession;
import model.SessionStatus;
import model.User;
import model.MentorshipProgram;
import service.MentorshipSessionService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MentorshipSessionServiceImpl extends UnicastRemoteObject implements MentorshipSessionService {

    private final MentorshipSessionDao mentorshipSessionDao;

    public MentorshipSessionServiceImpl() throws RemoteException {
        super();
        this.mentorshipSessionDao = new MentorshipSessionDaoImpl();
    }

    @Override
    public MentorshipSession registerMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException {
        mentorshipSessionDao.save(theMentorshipSession);
        return cleanSession(theMentorshipSession);
    }

    @Override
    public MentorshipSession updateMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException {
        mentorshipSessionDao.update(theMentorshipSession);
        return cleanSession(theMentorshipSession);
    }

    @Override
    public MentorshipSession deleteMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException {
        mentorshipSessionDao.delete(theMentorshipSession.getId());
        return cleanSession(theMentorshipSession);
    }

    @Override
    public MentorshipSession findMentorshipSessionRecordById(MentorshipSession theMentorshipSession) throws RemoteException {
        return cleanSession(mentorshipSessionDao.findById(theMentorshipSession.getId()));
    }

    @Override
    public List<MentorshipSession> findAllMentorshipSessionRecords() throws RemoteException {
        List<MentorshipSession> sessions = mentorshipSessionDao.findAll();
        if (sessions != null) {
            sessions.forEach(this::cleanSession);
        }
        return sessions;
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByMentorId(Long mentorId) throws RemoteException {
        List<MentorshipSession> sessions = mentorshipSessionDao.findByMentorId(mentorId);
        if (sessions != null) {
            sessions.forEach(this::cleanSession);
        }
        return sessions;
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByMenteeId(Long menteeId) throws RemoteException {
        List<MentorshipSession> sessions = mentorshipSessionDao.findByMenteeId(menteeId);
        if (sessions != null) {
            sessions.forEach(this::cleanSession);
        }
        return sessions;
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByStatus(SessionStatus status) throws RemoteException {
        List<MentorshipSession> sessions = mentorshipSessionDao.findByStatus(status);
        if (sessions != null) {
            sessions.forEach(this::cleanSession);
        }
        return sessions;
    }

    @Override
    public List<MentorshipSession> findUpcomingMentorshipSessionRecordsByUserId(Long userId) throws RemoteException {
        List<MentorshipSession> sessions = mentorshipSessionDao.findUpcomingByUserId(userId);
        if (sessions != null) {
            sessions.forEach(this::cleanSession);
        }
        return sessions;
    }

    private MentorshipSession cleanSession(MentorshipSession session) {
        if (session == null) return null;
        if (session.getMentor() != null) {
            session.setMentor(cleanUser(session.getMentor()));
        }
        if (session.getMentee() != null) {
            session.setMentee(cleanUser(session.getMentee()));
        }
        if (session.getProgram() != null) {
            MentorshipProgram program = session.getProgram();
            // Convert PersistentBag to a clean ArrayList if it exists
            if (program.getEnrolledUsers() != null) {
                java.util.List<User> cleanList = new java.util.ArrayList<>();
                for (User u : program.getEnrolledUsers()) {
                    cleanList.add(cleanUser(u));
                }
                program.setEnrolledUsers(cleanList);
            }
            if (program.getCreatedBy() != null) {
                program.setCreatedBy(cleanUser(program.getCreatedBy()));
            }
        }
        return session;
    }

    private User cleanUser(User user) {
        if (user == null) return null;
        user.setSessionsAsMentor(null);
        user.setSessionsAsMentee(null);
        user.setNotifications(null);
        user.setMentorProfile(null);
        return user;
    }
}
