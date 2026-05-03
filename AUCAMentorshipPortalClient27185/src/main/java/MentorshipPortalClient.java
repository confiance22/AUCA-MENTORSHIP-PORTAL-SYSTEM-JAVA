import service.*;
import util.ServiceRegistry;
import view.LoginView;

import javax.swing.SwingUtilities;
import java.rmi.Naming;

public class MentorshipPortalClient {

    public static void main(String[] args) {
        try {
            // Connect to RMI registry and lookup services
            ServiceRegistry.userService = (UserService) Naming.lookup("rmi://localhost:1099/UserService");
            ServiceRegistry.mentorProfileService = (MentorProfileService) Naming.lookup("rmi://localhost:1099/MentorProfileService");
            ServiceRegistry.mentorshipProgramService = (MentorshipProgramService) Naming.lookup("rmi://localhost:1099/MentorshipProgramService");
            ServiceRegistry.mentorshipSessionService = (MentorshipSessionService) Naming.lookup("rmi://localhost:1099/MentorshipSessionService");
            ServiceRegistry.notificationService = (NotificationService) Naming.lookup("rmi://localhost:1099/NotificationService");
            ServiceRegistry.reportService = (ReportService) Naming.lookup("rmi://localhost:1099/ReportService");

            System.out.println("Successfully connected to the AUCA Mentorship Portal Server (RMI)!");

            // Launch the Login View
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("Failed to connect to the server. Ensure the server is running.");
            e.printStackTrace();
        }
    }
}
