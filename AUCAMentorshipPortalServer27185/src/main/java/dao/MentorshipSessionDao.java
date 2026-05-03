package dao;

import model.MentorshipSession;
import model.SessionStatus;
import java.util.List;

public interface MentorshipSessionDao {

    void save(MentorshipSession session);
    void update(MentorshipSession session);
    void delete(Long id);
    MentorshipSession findById(Long id);
    List<MentorshipSession> findAll();
    List<MentorshipSession> findByMentorId(Long mentorId);
    List<MentorshipSession> findByMenteeId(Long menteeId);
    List<MentorshipSession> findByStatus(SessionStatus status);
    List<MentorshipSession> findUpcomingByUserId(Long userId);
}