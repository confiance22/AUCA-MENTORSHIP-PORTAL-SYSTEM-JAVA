package service;

import model.Notification;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NotificationService extends Remote {
    Notification registerNotificationRecord(Notification theNotification) throws RemoteException;
    Notification updateNotificationRecord(Notification theNotification) throws RemoteException;
    Notification deleteNotificationRecord(Notification theNotification) throws RemoteException;
    Notification findNotificationRecordById(Notification theNotification) throws RemoteException;
    List<Notification> findAllNotificationRecords() throws RemoteException;
    
    // Additional DAO methods
    List<Notification> findNotificationRecordsByUserId(Long userId) throws RemoteException;
    Notification findValidOtpRecord(Long userId, String otpCode) throws RemoteException;
    void markAllReadNotificationRecordsByUserId(Long userId) throws RemoteException;
    void markNotificationAsRead(Long notificationId) throws RemoteException;
    
    // Custom method to send OTP
    void sendOtpNotification(Long userId, String email) throws RemoteException;
    void notifyAdmins(String message) throws RemoteException;
}
