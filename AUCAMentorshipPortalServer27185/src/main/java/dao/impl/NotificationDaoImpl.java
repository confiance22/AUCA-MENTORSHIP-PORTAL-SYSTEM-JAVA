package dao.impl;

import dao.NotificationDao;
import model.Notification;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

    @Override
    public void save(Notification notification) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(notification);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Database error saving notification: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Notification notification) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(notification);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Notification notification = session.get(Notification.class, id);
            if (notification != null) {
                session.remove(notification);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Notification findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Notification.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Notification> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Notification", Notification.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Notification WHERE user.id = :userId ORDER BY createdAt DESC",
                Notification.class)
                .setParameter("userId", userId)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Notification findValidOtp(Long userId, String otpCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Notification WHERE user.id = :userId AND otpCode = :otpCode " +
                "AND isUsed = false AND expiresAt > :now", Notification.class)
                .setParameter("userId", userId)
                .setParameter("otpCode", otpCode)
                .setParameter("now", LocalDateTime.now())
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void markAllReadByUserId(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery(
                "UPDATE Notification SET isRead = true WHERE user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}