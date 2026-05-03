package dao.impl;

import dao.MentorshipSessionDao;
import model.MentorshipSession;
import model.SessionStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.time.LocalDateTime;
import java.util.List;

public class MentorshipSessionDaoImpl implements MentorshipSessionDao {

    @Override
    public void save(MentorshipSession mentorshipSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(mentorshipSession);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MentorshipSession mentorshipSession) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(mentorshipSession);
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
            MentorshipSession mentorshipSession = session.get(MentorshipSession.class, id);
            if (mentorshipSession != null) {
                session.remove(mentorshipSession);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MentorshipSession findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MentorshipSession.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipSession> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM MentorshipSession", MentorshipSession.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipSession> findByMentorId(Long mentorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM MentorshipSession WHERE mentor.id = :mentorId", MentorshipSession.class)
                .setParameter("mentorId", mentorId)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipSession> findByMenteeId(Long menteeId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM MentorshipSession WHERE mentee.id = :menteeId", MentorshipSession.class)
                .setParameter("menteeId", menteeId)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipSession> findByStatus(SessionStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM MentorshipSession WHERE status = :status", MentorshipSession.class)
                .setParameter("status", status)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipSession> findUpcomingByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM MentorshipSession WHERE (mentor.id = :userId OR mentee.id = :userId) " +
                "AND scheduledAt > :now AND status = :status", MentorshipSession.class)
                .setParameter("userId", userId)
                .setParameter("now", LocalDateTime.now())
                .setParameter("status", SessionStatus.SCHEDULED)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}