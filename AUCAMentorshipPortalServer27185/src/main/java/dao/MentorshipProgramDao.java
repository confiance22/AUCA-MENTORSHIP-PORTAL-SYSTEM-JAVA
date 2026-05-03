package dao;

import model.MentorshipProgram;
import model.ProgramStatus;
import java.util.List;

public interface MentorshipProgramDao {

    void save(MentorshipProgram program);
    void update(MentorshipProgram program);
    void delete(Long id);
    MentorshipProgram findById(Long id);
    List<MentorshipProgram> findAll();
    List<MentorshipProgram> findByStatus(ProgramStatus status);
    List<MentorshipProgram> findByUserId(Long userId);
    int countEnrolled(Long programId);
}