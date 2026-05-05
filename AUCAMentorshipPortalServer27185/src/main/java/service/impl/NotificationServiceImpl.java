package service.impl;

import dao.NotificationDao;
import dao.impl.NotificationDaoImpl;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import messaging.ActiveMQProducer;
import model.Notification;
import model.NotificationType;
import model.User;
import service.NotificationService;
import util.OTPUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationServiceImpl extends UnicastRemoteObject implements NotificationService {

    private final NotificationDao notificationDao;
    private final UserDao userDao;

    public NotificationServiceImpl() throws RemoteException {
        super();
        this.notificationDao = new NotificationDaoImpl();
        this.userDao = new UserDaoImpl();
    }

    @Override
    public Notification registerNotificationRecord(Notification theNotification) throws RemoteException {
        notificationDao.save(theNotification);
        return cleanNotification(theNotification);
    }

    @Override
    public Notification updateNotificationRecord(Notification theNotification) throws RemoteException {
        notificationDao.update(theNotification);
        return cleanNotification(theNotification);
    }

    @Override
    public Notification deleteNotificationRecord(Notification theNotification) throws RemoteException {
        notificationDao.delete(theNotification.getId());
        return cleanNotification(theNotification);
    }

    @Override
    public Notification findNotificationRecordById(Notification theNotification) throws RemoteException {
        return cleanNotification(notificationDao.findById(theNotification.getId()));
    }

    @Override
    public List<Notification> findAllNotificationRecords() throws RemoteException {
        List<Notification> notifs = notificationDao.findAll();
        if (notifs != null) notifs.forEach(this::cleanNotification);
        return notifs;
    }

    @Override
    public List<Notification> findNotificationRecordsByUserId(Long userId) throws RemoteException {
        List<Notification> notifs = notificationDao.findByUserId(userId);
        if (notifs != null) notifs.forEach(this::cleanNotification);
        return notifs;
    }

    @Override
    public Notification findValidOtpRecord(Long userId, String otpCode) throws RemoteException {
        return cleanNotification(notificationDao.findValidOtp(userId, otpCode));
    }

    @Override
    public void markAllReadNotificationRecordsByUserId(Long userId) throws RemoteException {
        notificationDao.markAllReadByUserId(userId);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) throws RemoteException {
        Notification n = notificationDao.findById(notificationId);
        if (n != null) {
            n.setRead(true);
            notificationDao.update(n);
        }
    }

    @Override
    public void sendOtpNotification(Long userId, String email) throws RemoteException {
        try {
            User user = userDao.findById(userId);
            if (user == null) {
                throw new RemoteException("User not found");
            }

            String otp = OTPUtil.generateOTP();

            // Create notification
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType(NotificationType.OTP);
            notification.setMessage("Your OTP verification code is: " + otp);
            notification.setOtpCode(otp);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
            notification.setUsed(false);
            notification.setRead(false);
            
            notificationDao.save(notification);

            // Send via ActiveMQ
            String message = "OTP|" + email + "|" + otp;
            ActiveMQProducer.sendNotification(message);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error sending OTP: " + e.getMessage());
        }
    }

    @Override
    public void notifyAdmins(String message) throws RemoteException {
        try {
            List<User> admins = userDao.findByRole(model.UserRole.ADMIN);
            if (admins != null) {
                for (User admin : admins) {
                    Notification notif = new Notification();
                    notif.setUser(admin);
                    notif.setMessage(message);
                    notif.setType(NotificationType.GENERAL);
                    notif.setCreatedAt(LocalDateTime.now());
                    notif.setRead(false);
                    notificationDao.save(notif);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to notify admins: " + e.getMessage());
        }
    }

    private Notification cleanNotification(Notification notification) {
        if (notification == null) return null;
        if (notification.getUser() != null) {
            User user = notification.getUser();
            user.setSessionsAsMentor(null);
            user.setSessionsAsMentee(null);
            user.setNotifications(null);
            user.setMentorProfile(null);
        }
        return notification;
    }
}
