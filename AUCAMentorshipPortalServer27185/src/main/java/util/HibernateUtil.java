package util;

import model.MentorProfile;
import model.MentorshipProgram;
import model.MentorshipSession;
import model.Notification;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(MentorProfile.class);
                configuration.addAnnotatedClass(MentorshipProgram.class);
                configuration.addAnnotatedClass(MentorshipSession.class);
                configuration.addAnnotatedClass(Notification.class);

                sessionFactory = configuration.buildSessionFactory();
                System.out.println("Hibernate SessionFactory created successfully.");

            } catch (Exception e) {
                System.out.println("Error creating Hibernate SessionFactory: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Hibernate SessionFactory closed.");
        }
    }
}