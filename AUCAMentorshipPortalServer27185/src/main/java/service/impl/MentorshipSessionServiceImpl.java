package service.impl;

import dao.MentorshipSessionDao;
import dao.impl.MentorshipSessionDaoImpl;
import model.MentorshipSession;
import model.SessionStatus;
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
        return theMentorshipSession;
    }

    @Override
    public MentorshipSession updateMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException {
        mentorshipSessionDao.update(theMentorshipSession);
        return theMentorshipSession;
    }

    @Override
    public MentorshipSession deleteMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException {
        mentorshipSessionDao.delete(theMentorshipSession.getId());
        return theMentorshipSession;
    }

    @Override
    public MentorshipSession findMentorshipSessionRecordById(MentorshipSession theMentorshipSession) throws RemoteException {
        return mentorshipSessionDao.findById(theMentorshipSession.getId());
    }

    @Override
    public List<MentorshipSession> findAllMentorshipSessionRecords() throws RemoteException {
        return mentorshipSessionDao.findAll();
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByMentorId(Long mentorId) throws RemoteException {
        return mentorshipSessionDao.findByMentorId(mentorId);
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByMenteeId(Long menteeId) throws RemoteException {
        return mentorshipSessionDao.findByMenteeId(menteeId);
    }

    @Override
    public List<MentorshipSession> findMentorshipSessionRecordsByStatus(SessionStatus status) throws RemoteException {
        return mentorshipSessionDao.findByStatus(status);
    }

    @Override
    public List<MentorshipSession> findUpcomingMentorshipSessionRecordsByUserId(Long userId) throws RemoteException {
        return mentorshipSessionDao.findUpcomingByUserId(userId);
    }
}
