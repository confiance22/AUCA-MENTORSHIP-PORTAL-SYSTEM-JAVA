package dao;

import model.User;
import model.UserRole;
import java.util.List;

public interface UserDao {

    void save(User user);
    void update(User user);
    void delete(Long id);
    User findById(Long id);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findByRole(UserRole role);
    boolean existsByEmail(String email);
}