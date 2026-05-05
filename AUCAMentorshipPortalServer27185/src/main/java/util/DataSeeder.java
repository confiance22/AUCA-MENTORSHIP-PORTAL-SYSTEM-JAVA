package util;

import dao.impl.UserDaoImpl;
import model.User;
import model.UserRole;
import java.time.LocalDateTime;

/**
 * DataSeeder runs automatically when the server starts.
 * It ensures the default admin account exists in the database
 * with a properly BCrypt-hashed password.
 *
 * Admin credentials:
 *   Email    : admin@auca.ac.rw
 *   Password : Admin@1234
 */
public class DataSeeder {

    private static final String ADMIN_EMAIL     = "admin@auca.ac.rw";
    private static final String ADMIN_PASSWORD  = "Admin@1234";
    private static final String ADMIN_FIRST     = "System";
    private static final String ADMIN_LAST      = "Admin";
    private static final String ADMIN_PHONE     = "0780000000";

    public static void seed() {
        UserDaoImpl userDao = new UserDaoImpl();

        try {
            boolean adminExists = userDao.existsByEmail(ADMIN_EMAIL);

            if (!adminExists) {
                // Create admin from scratch
                User admin = new User();
                admin.setFirstName(ADMIN_FIRST);
                admin.setLastName(ADMIN_LAST);
                admin.setEmail(ADMIN_EMAIL);
                admin.setPassword(PasswordUtil.hashPassword(ADMIN_PASSWORD));
                admin.setPhoneNumber(ADMIN_PHONE);
                admin.setRole(UserRole.ADMIN);
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());

                userDao.save(admin);
                System.out.println("[DataSeeder] Admin account created successfully.");
                System.out.println("[DataSeeder]   Email    : " + ADMIN_EMAIL);
                System.out.println("[DataSeeder]   Password : " + ADMIN_PASSWORD);

            } else {
                // Admin exists — fix the password hash if it is still the placeholder
                User existingAdmin = userDao.findByEmail(ADMIN_EMAIL);
                boolean hashIsValid = existingAdmin.getPassword() != null
                        && existingAdmin.getPassword().startsWith("$2a$")
                        && existingAdmin.getPassword().length() == 60;

                if (!hashIsValid) {
                    // Hash is broken/placeholder — overwrite with a real one
                    existingAdmin.setPassword(PasswordUtil.hashPassword(ADMIN_PASSWORD));
                    userDao.update(existingAdmin);
                    System.out.println("[DataSeeder] Admin password hash was invalid — fixed automatically.");
                    System.out.println("[DataSeeder]   Email    : " + ADMIN_EMAIL);
                    System.out.println("[DataSeeder]   Password : " + ADMIN_PASSWORD);
                } else {
                    System.out.println("[DataSeeder] Admin account already exists and is valid. No action needed.");
                }
            }

            // Seed Sample Mentor
            if (!userDao.existsByEmail("mentor@auca.ac.rw")) {
                User mentor = new User();
                mentor.setFirstName("Sample");
                mentor.setLastName("Mentor");
                mentor.setEmail("mentor@auca.ac.rw");
                mentor.setPassword(PasswordUtil.hashPassword("Mentor@1234"));
                mentor.setPhoneNumber("0781111111");
                mentor.setRole(UserRole.MENTOR);
                mentor.setIsActive(true);
                mentor.setCreatedAt(LocalDateTime.now());
                userDao.save(mentor);
                System.out.println("[DataSeeder] Sample Mentor account created: mentor@auca.ac.rw / Mentor@1234");
            }

            // Seed Sample Mentee
            if (!userDao.existsByEmail("mentee@auca.ac.rw")) {
                User mentee = new User();
                mentee.setFirstName("Sample");
                mentee.setLastName("Mentee");
                mentee.setEmail("mentee@auca.ac.rw");
                mentee.setPassword(PasswordUtil.hashPassword("Mentee@1234"));
                mentee.setPhoneNumber("0782222222");
                mentee.setRole(UserRole.MENTEE);
                mentee.setIsActive(true);
                mentee.setCreatedAt(LocalDateTime.now());
                userDao.save(mentee);
                System.out.println("[DataSeeder] Sample Mentee account created: mentee@auca.ac.rw / Mentee@1234");
            }

        } catch (Exception e) {
            System.err.println("[DataSeeder] ERROR during seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
