package dao;

import model.MentorshipFeedback;
import java.util.List;

public interface MentorshipFeedbackDao {
    void save(MentorshipFeedback feedback);
    void update(MentorshipFeedback feedback);
    void delete(Long id);
    MentorshipFeedback findById(Long id);
    List<MentorshipFeedback> findAll();
    MentorshipFeedback findBySessionId(Long sessionId);
    List<MentorshipFeedback> findByMentorId(Long mentorId);
}
