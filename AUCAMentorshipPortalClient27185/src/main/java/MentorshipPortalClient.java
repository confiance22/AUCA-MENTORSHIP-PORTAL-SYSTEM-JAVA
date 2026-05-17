import com.formdev.flatlaf.FlatDarkLaf;
import service.*;
import util.ServiceRegistry;
import view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.rmi.Naming;

public class MentorshipPortalClient {

    public static void main(String[] args) {
        try {
            // Setup FlatLaf Dark Theme
            FlatDarkLaf.setup();
            
            // Global Component Curves (Linear-style roundness)
            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("ProgressBar.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            
            // Global Obsidian Palette Integration
            UIManager.put("background", util.UITheme.BACKGROUND_MAIN);
            UIManager.put("Panel.background", util.UITheme.BACKGROUND_MAIN);
            UIManager.put("Button.background", util.UITheme.BACKGROUND_CARD);
            UIManager.put("Button.foreground", util.UITheme.TEXT_PRIMARY);
            UIManager.put("Component.background", util.UITheme.BACKGROUND_CARD);
            UIManager.put("Component.foreground", util.UITheme.TEXT_PRIMARY);
            UIManager.put("TextComponent.background", util.UITheme.BACKGROUND_CARD);
            UIManager.put("TextComponent.foreground", util.UITheme.TEXT_PRIMARY);
            UIManager.put("TableHeader.background", util.UITheme.BACKGROUND_MAIN);
            UIManager.put("TableHeader.foreground", util.UITheme.TEXT_MUTED);
            UIManager.put("Table.background", util.UITheme.BACKGROUND_CARD);
            UIManager.put("Table.gridColor", util.UITheme.BORDER_COLOR);
            UIManager.put("ScrollPane.background", util.UITheme.BACKGROUND_MAIN);
            
            // Focused Glow Effect
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.focusColor", util.UITheme.ACCENT_INDIGO);
            UIManager.put("Component.innerFocusWidth", 0);
            
            // Dialogs Override
            UIManager.put("OptionPane.background", util.UITheme.BACKGROUND_CARD);
            UIManager.put("OptionPane.messageForeground", util.UITheme.TEXT_PRIMARY);
            UIManager.put("OptionPane.warningIcon", null); // Minimize visual clutter
            UIManager.put("OptionPane.errorIcon", null);
            UIManager.put("OptionPane.informationIcon", null);
            UIManager.put("OptionPane.questionIcon", null);
            
        } catch(Exception ex) {
            System.err.println("Failed to initialize LaF: " + ex.getMessage());
        }

        try {
            // Connect to RMI registry and lookup services on port 5099
            ServiceRegistry.userService = (UserService) Naming.lookup("rmi://localhost:5099/UserService");
            ServiceRegistry.mentorProfileService = (MentorProfileService) Naming.lookup("rmi://localhost:5099/MentorProfileService");
            ServiceRegistry.mentorshipProgramService = (MentorshipProgramService) Naming.lookup("rmi://localhost:5099/MentorshipProgramService");
            ServiceRegistry.mentorshipSessionService = (MentorshipSessionService) Naming.lookup("rmi://localhost:5099/MentorshipSessionService");
            ServiceRegistry.notificationService = (NotificationService) Naming.lookup("rmi://localhost:5099/NotificationService");
            ServiceRegistry.reportService = (ReportService) Naming.lookup("rmi://localhost:5099/ReportService");
            ServiceRegistry.mentorshipFeedbackService = (MentorshipFeedbackService) Naming.lookup("rmi://localhost:5099/MentorshipFeedbackService");

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
