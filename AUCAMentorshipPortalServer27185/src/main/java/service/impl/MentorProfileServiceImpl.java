package service.impl;

import dao.MentorProfileDao;
import dao.impl.MentorProfileDaoImpl;
import model.MentorProfile;
import service.MentorProfileService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MentorProfileServiceImpl extends UnicastRemoteObject implements MentorProfileService {

    private final MentorProfileDao mentorProfileDao;

    public MentorProfileServiceImpl() throws RemoteException {
        super();
        this.mentorProfileDao = new MentorProfileDaoImpl();
    }

    @Override
    public MentorProfile registerMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException {
        mentorProfileDao.save(theMentorProfile);
        return theMentorProfile;
    }

    @Override
    public MentorProfile updateMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException {
        mentorProfileDao.update(theMentorProfile);
        return theMentorProfile;
    }

    @Override
    public MentorProfile deleteMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException {
        mentorProfileDao.delete(theMentorProfile.getId());
        return theMentorProfile;
    }

    @Override
    public MentorProfile findMentorProfileRecordById(MentorProfile theMentorProfile) throws RemoteException {
        return mentorProfileDao.findById(theMentorProfile.getId());
    }

    @Override
    public List<MentorProfile> findAllMentorProfileRecords() throws RemoteException {
        return mentorProfileDao.findAll();
    }

    @Override
    public MentorProfile findMentorProfileRecordByUserId(Long userId) throws RemoteException {
        return mentorProfileDao.findByUserId(userId);
    }

    @Override
    public List<MentorProfile> findAvailableMentorProfileRecords() throws RemoteException {
        return mentorProfileDao.findAvailableMentors();
    }
}
