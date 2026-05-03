package service;

import model.MentorshipProgram;
import model.ProgramStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MentorshipProgramService extends Remote {
    MentorshipProgram registerMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException;
    MentorshipProgram updateMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException;
    MentorshipProgram deleteMentorshipProgramRecord(MentorshipProgram theMentorshipProgram) throws RemoteException;
    MentorshipProgram findMentorshipProgramRecordById(MentorshipProgram theMentorshipProgram) throws RemoteException;
    List<MentorshipProgram> findAllMentorshipProgramRecords() throws RemoteException;
    
    // Additional DAO methods
    List<MentorshipProgram> findMentorshipProgramRecordsByStatus(ProgramStatus status) throws RemoteException;
    List<MentorshipProgram> findMentorshipProgramRecordsByUserId(Long userId) throws RemoteException;
    int countEnrolledMentorshipProgram(Long programId) throws RemoteException;
}
