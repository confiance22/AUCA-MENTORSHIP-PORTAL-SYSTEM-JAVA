package dao.impl;

import dao.MentorshipFeedbackDao;
import model.MentorshipFeedback;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class MentorshipFeedbackDaoImpl implements MentorshipFeedbackDao {

    @Override
    public void save(MentorshipFeedback feedback) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(feedback);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(MentorshipFeedback feedback) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(feedback);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            MentorshipFeedback feedback = session.get(MentorshipFeedback.class, id);
            if (feedback != null) session.remove(feedback);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public MentorshipFeedback findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT f FROM MentorshipFeedback f LEFT JOIN FETCH f.session s LEFT JOIN FETCH s.mentor LEFT JOIN FETCH s.mentee WHERE f.id = :id", MentorshipFeedback.class)
                .setParameter("id", id)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipFeedback> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT f FROM MentorshipFeedback f LEFT JOIN FETCH f.session s LEFT JOIN FETCH s.mentor LEFT JOIN FETCH s.mentee", MentorshipFeedback.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MentorshipFeedback findBySessionId(Long sessionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT f FROM MentorshipFeedback f WHERE f.session.id = :sessionId", MentorshipFeedback.class)
                .setParameter("sessionId", sessionId)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipFeedback> findByMentorId(Long mentorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT f FROM MentorshipFeedback f WHERE f.session.mentor.id = :mentorId", MentorshipFeedback.class)
                .setParameter("mentorId", mentorId)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
