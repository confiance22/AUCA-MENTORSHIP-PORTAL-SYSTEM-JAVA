package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {

    private User currentUser;
    private JPanel contentPanel;

    public DashboardView(User user) {
        this.currentUser = user;
        setTitle("AUCA Mentorship Portal - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 122, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("AUCA Mentorship Portal");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " (" + currentUser.getRole() + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(userLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Sidebar Navigation Panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(240, 240, 240));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Main Content Panel with CardLayout
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Add Welcome Panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("<html><h2>Dashboard Overview</h2><p>Welcome to the AUCA Mentorship Portal. Select an option from the sidebar to begin.</p></html>");
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        contentPanel.add(welcomePanel, "Welcome");

        // Role-Specific Buttons and Modules
        switch (currentUser.getRole()) {
            case ADMIN:
                addSidebarButton(sidebarPanel, "User Management", "Users", new UserModule(currentUser));
                addSidebarButton(sidebarPanel, "Manage Programs", "Programs", new ProgramModule(currentUser));
                addSidebarButton(sidebarPanel, "System Reports", "Reports", new ReportModule(currentUser));
                addSidebarButton(sidebarPanel, "System Alerts", "Notifications", new NotificationModule(currentUser));
                break;
            case MENTOR:
                addSidebarButton(sidebarPanel, "My Mentor Profile", "Profile", new MentorProfileModule(currentUser));
                addSidebarButton(sidebarPanel, "My Programs", "Programs", new ProgramModule(currentUser));
                addSidebarButton(sidebarPanel, "My Sessions", "Sessions", new SessionModule(currentUser));
                addSidebarButton(sidebarPanel, "My Notifications", "Notifications", new NotificationModule(currentUser));
                addSidebarButton(sidebarPanel, "Performance Reports", "Reports", new ReportModule(currentUser));
                break;
            case MENTEE:
                addSidebarButton(sidebarPanel, "Browse Programs", "Programs", new ProgramModule(currentUser));
                addSidebarButton(sidebarPanel, "My Sessions", "Sessions", new SessionModule(currentUser));
                addSidebarButton(sidebarPanel, "My Notifications", "Notifications", new NotificationModule(currentUser));
                addSidebarButton(sidebarPanel, "My Profile", "Profile", new MentorProfileModule(currentUser));
                break;
        }

        sidebarPanel.add(Box.createVerticalGlue());

        JButton logoutBtn = createSidebarButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        sidebarPanel.add(logoutBtn);

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addSidebarButton(JPanel panel, String text, String moduleName, JPanel module) {
        JButton btn = createSidebarButton(text);
        btn.addActionListener(e -> switchModule(moduleName));
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(module, moduleName);
    }

    private void switchModule(String moduleName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, moduleName);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        return button;
    }
}
