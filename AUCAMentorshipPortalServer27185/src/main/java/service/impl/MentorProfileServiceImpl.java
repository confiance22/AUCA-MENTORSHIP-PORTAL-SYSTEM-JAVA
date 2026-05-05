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
        return cleanProfile(theMentorProfile);
    }

    @Override
    public MentorProfile updateMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException {
        mentorProfileDao.update(theMentorProfile);
        return cleanProfile(theMentorProfile);
    }

    @Override
    public MentorProfile deleteMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException {
        mentorProfileDao.delete(theMentorProfile.getId());
        return cleanProfile(theMentorProfile);
    }

    @Override
    public MentorProfile findMentorProfileRecordById(MentorProfile theMentorProfile) throws RemoteException {
        return cleanProfile(mentorProfileDao.findById(theMentorProfile.getId()));
    }

    @Override
    public List<MentorProfile> findAllMentorProfileRecords() throws RemoteException {
        List<MentorProfile> profiles = mentorProfileDao.findAll();
        if (profiles != null) profiles.forEach(this::cleanProfile);
        return profiles;
    }

    @Override
    public MentorProfile findMentorProfileRecordByUserId(Long userId) throws RemoteException {
        return cleanProfile(mentorProfileDao.findByUserId(userId));
    }

    @Override
    public List<MentorProfile> findAvailableMentorProfileRecords() throws RemoteException {
        List<MentorProfile> profiles = mentorProfileDao.findAvailableMentors();
        if (profiles != null) profiles.forEach(this::cleanProfile);
        return profiles;
    }

    private MentorProfile cleanProfile(MentorProfile profile) {
        if (profile == null) return null;
        if (profile.getUser() != null) {
            model.User user = profile.getUser();
            user.setSessionsAsMentor(null);
            user.setSessionsAsMentee(null);
            user.setNotifications(null);
            user.setMentorProfile(null);
        }
        return profile;
    }
}
