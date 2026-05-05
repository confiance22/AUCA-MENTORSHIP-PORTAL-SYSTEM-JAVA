package service.impl;

import dao.MentorshipProgramDao;
import dao.impl.MentorshipProgramDaoImpl;
import model.MentorshipProgram;
import model.ProgramStatus;
import model.User;
import service.MentorshipProgramService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MentorshipProgramServiceImpl extends UnicastRemoteObject implements MentorshipProgramService {

    private final MentorshipProgramDao mentorshipProgramDao;

    public MentorshipProgramServiceImpl() throws RemoteException {
        super();
        this.mentorshipProgramDao = new MentorshipProgramDaoImpl();
    }

    @Override
    public MentorshipProgram registerMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException {
        mentorshipProgramDao.save(theMentorshipProgram);
        return cleanProgram(theMentorshipProgram);
    }

    @Override
    public MentorshipProgram updateMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException {
        mentorshipProgramDao.update(theMentorshipProgram);
        return cleanProgram(theMentorshipProgram);
    }

    @Override
    public MentorshipProgram deleteMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException {
        mentorshipProgramDao.delete(theMentorshipProgram.getId());
        return cleanProgram(theMentorshipProgram);
    }

    @Override
    public MentorshipProgram findMentorshipProgramRecordById(MentorshipProgram theMentorshipProgram) throws RemoteException {
        return cleanProgram(mentorshipProgramDao.findById(theMentorshipProgram.getId()));
    }

    @Override
    public List<MentorshipProgram> findAllMentorshipProgramRecords() throws RemoteException {
        List<MentorshipProgram> programs = mentorshipProgramDao.findAll();
        if (programs != null) {
            programs.forEach(this::cleanProgram);
        }
        return programs;
    }

    @Override
    public List<MentorshipProgram> findMentorshipProgramRecordsByStatus(ProgramStatus status) throws RemoteException {
        List<MentorshipProgram> programs = mentorshipProgramDao.findByStatus(status);
        if (programs != null) {
            programs.forEach(this::cleanProgram);
        }
        return programs;
    }

    @Override
    public List<MentorshipProgram> findMentorshipProgramRecordsByUserId(Long userId) throws RemoteException {
        List<MentorshipProgram> programs = mentorshipProgramDao.findByUserId(userId);
        if (programs != null) {
            programs.forEach(this::cleanProgram);
        }
        return programs;
    }

    @Override
    public int countEnrolledMentorshipProgram(Long programId) throws RemoteException {
        return mentorshipProgramDao.countEnrolled(programId);
    }

    private MentorshipProgram cleanProgram(MentorshipProgram program) {
        if (program == null) return null;
        
        // Convert PersistentBag to a clean ArrayList
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
        return program;
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
