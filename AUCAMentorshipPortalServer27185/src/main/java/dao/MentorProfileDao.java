package dao;

import model.MentorProfile;
import java.util.List;

public interface MentorProfileDao {

    void save(MentorProfile mentorProfile);
    void update(MentorProfile mentorProfile);
    void delete(Long id);
    MentorProfile findById(Long id);
    MentorProfile findByUserId(Long userId);
    List<MentorProfile> findAll();
    List<MentorProfile> findAvailableMentors();
}