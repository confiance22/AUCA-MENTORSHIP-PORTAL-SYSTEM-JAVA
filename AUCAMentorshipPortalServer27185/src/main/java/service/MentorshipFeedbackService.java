package service;

import model.MentorshipFeedback;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MentorshipFeedbackService extends Remote {
    MentorshipFeedback registerFeedbackRecord(MentorshipFeedback feedback) throws RemoteException;
    MentorshipFeedback updateFeedbackRecord(MentorshipFeedback feedback) throws RemoteException;
    MentorshipFeedback deleteFeedbackRecord(MentorshipFeedback feedback) throws RemoteException;
    MentorshipFeedback findFeedbackRecordById(Long id) throws RemoteException;
    List<MentorshipFeedback> findAllFeedbackRecords() throws RemoteException;
    MentorshipFeedback findFeedbackRecordBySessionId(Long sessionId) throws RemoteException;
    List<MentorshipFeedback> findFeedbackRecordsByMentorId(Long mentorId) throws RemoteException;
}
