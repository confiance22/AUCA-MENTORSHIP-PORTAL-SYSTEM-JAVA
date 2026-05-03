package service;

import model.MentorProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MentorProfileService extends Remote {
    MentorProfile registerMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException;
    MentorProfile updateMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException;
    MentorProfile deleteMentorProfileRecord(MentorProfile theMentorProfile) throws RemoteException;
    MentorProfile findMentorProfileRecordById(MentorProfile theMentorProfile) throws RemoteException;
    List<MentorProfile> findAllMentorProfileRecords() throws RemoteException;
    
    // Additional DAO methods
    MentorProfile findMentorProfileRecordByUserId(Long userId) throws RemoteException;
    List<MentorProfile> findAvailableMentorProfileRecords() throws RemoteException;
}
