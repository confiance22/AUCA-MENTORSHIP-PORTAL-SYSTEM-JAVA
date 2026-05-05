package dao.impl;

import dao.MentorshipProgramDao;
import model.MentorshipProgram;
import model.ProgramStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.List;

public class MentorshipProgramDaoImpl implements MentorshipProgramDao {

    @Override
    public void save(MentorshipProgram program) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(program);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void update(MentorshipProgram program) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(program);
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
            MentorshipProgram program = session.get(MentorshipProgram.class, id);
            if (program != null) {
                session.remove(program);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public MentorshipProgram findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT p FROM MentorshipProgram p LEFT JOIN FETCH p.enrolledUsers LEFT JOIN FETCH p.createdBy WHERE p.id = :id",
                MentorshipProgram.class)
                .setParameter("id", id)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipProgram> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT p FROM MentorshipProgram p LEFT JOIN FETCH p.enrolledUsers LEFT JOIN FETCH p.createdBy", MentorshipProgram.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipProgram> findByStatus(ProgramStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM MentorshipProgram WHERE status = :status", MentorshipProgram.class)
                .setParameter("status", status)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MentorshipProgram> findByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT DISTINCT p FROM MentorshipProgram p LEFT JOIN FETCH p.enrolledUsers LEFT JOIN FETCH p.createdBy WHERE p.createdBy.id = :userId OR EXISTS (SELECT 1 FROM p.enrolledUsers u WHERE u.id = :userId)",
                MentorshipProgram.class)
                .setParameter("userId", userId)
                .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int countEnrolled(Long programId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                "SELECT COUNT(u) FROM MentorshipProgram p JOIN p.enrolledUsers u WHERE p.id = :programId",
                Long.class)
                .setParameter("programId", programId)
                .uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}