package util;

/**
 * Run this class ONCE to generate a BCrypt hash for the admin password.
 * Copy the printed hash and paste it into your SQL seed script.
 * You can delete this file after use.
 */
public class GenerateAdminHash {

    public static void main(String[] args) {
        String plainPassword = "Admin@1234";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        System.out.println("=================================================");
        System.out.println("Plain Password : " + plainPassword);
        System.out.println("BCrypt Hash    : " + hashedPassword);
        System.out.println("=================================================");
        System.out.println();
        System.out.println("--- Paste this into your SQL script ---");
        System.out.println("UPDATE users SET password = '" + hashedPassword + "' WHERE email = 'admin@auca.ac.rw';");
        System.out.println("---------------------------------------");
    }
}
