package dao;

import model.Notification;
import java.util.List;

public interface NotificationDao {

    void save(Notification notification);
    void update(Notification notification);
    void delete(Long id);
    Notification findById(Long id);
    List<Notification> findAll();
    List<Notification> findByUserId(Long userId);
    Notification findValidOtp(Long userId, String otpCode);
    void markAllReadByUserId(Long userId);
}