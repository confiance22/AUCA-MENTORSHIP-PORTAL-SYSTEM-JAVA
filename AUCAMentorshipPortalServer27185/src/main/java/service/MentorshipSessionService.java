package service;

import model.MentorshipSession;
import model.SessionStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MentorshipSessionService extends Remote {
    MentorshipSession registerMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException;
    MentorshipSession updateMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException;
    MentorshipSession deleteMentorshipSessionRecord(MentorshipSession theMentorshipSession) throws RemoteException;
    MentorshipSession findMentorshipSessionRecordById(MentorshipSession theMentorshipSession) throws RemoteException;
    List<MentorshipSession> findAllMentorshipSessionRecords() throws RemoteException;
    
    // Additional DAO methods
    List<MentorshipSession> findMentorshipSessionRecordsByMentorId(Long mentorId) throws RemoteException;
    List<MentorshipSession> findMentorshipSessionRecordsByMenteeId(Long menteeId) throws RemoteException;
    List<MentorshipSession> findMentorshipSessionRecordsByStatus(SessionStatus status) throws RemoteException;
    List<MentorshipSession> findUpcomingMentorshipSessionRecordsByUserId(Long userId) throws RemoteException;
}
