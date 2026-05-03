package service.impl;

import dao.MentorshipProgramDao;
import dao.impl.MentorshipProgramDaoImpl;
import model.MentorshipProgram;
import model.ProgramStatus;
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
        return theMentorshipProgram;
    }

    @Override
    public MentorshipProgram updateMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException {
        mentorshipProgramDao.update(theMentorshipProgram);
        return theMentorshipProgram;
    }

    @Override
    public MentorshipProgram deleteMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException {
        mentorshipProgramDao.delete(theMentorshipProgram.getId());
        return theMentorshipProgram;
    }

    @Override
    public MentorshipProgram findMentorshipProgramRecordById(MentorshipProgram theMentorshipProgram) throws RemoteException {
        return mentorshipProgramDao.findById(theMentorshipProgram.getId());
    }

    @Override
    public List<MentorshipProgram> findAllMentorshipProgramRecords() throws RemoteException {
        return mentorshipProgramDao.findAll();
    }

    @Override
    public List<MentorshipProgram> findMentorshipProgramRecordsByStatus(ProgramStatus status) throws RemoteException {
        return mentorshipProgramDao.findByStatus(status);
    }

    @Override
    public List<MentorshipProgram> findMentorshipProgramRecordsByUserId(Long userId) throws RemoteException {
        return mentorshipProgramDao.findByUserId(userId);
    }

    @Override
    public int countEnrolledMentorshipProgram(Long programId) throws RemoteException {
        return mentorshipProgramDao.countEnrolled(programId);
    }
}
