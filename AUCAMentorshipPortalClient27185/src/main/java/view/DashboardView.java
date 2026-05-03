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
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton profileBtn = createSidebarButton("My Profile");
        JButton programsBtn = createSidebarButton("Mentorship Programs");
        JButton sessionsBtn = createSidebarButton("My Sessions");
        JButton logoutBtn = createSidebarButton("Logout");

        profileBtn.addActionListener(e -> switchModule("Profile"));
        programsBtn.addActionListener(e -> switchModule("Programs"));
        sessionsBtn.addActionListener(e -> switchModule("Sessions"));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        sidebarPanel.add(profileBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(programsBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(sessionsBtn);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(logoutBtn);

        add(sidebarPanel, BorderLayout.WEST);

        // Main Content Panel with CardLayout
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("<html><h2>Dashboard Overview</h2><p>Select an option from the sidebar to begin.</p></html>");
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        
        contentPanel.add(welcomePanel, "Welcome");
        contentPanel.add(new MentorProfileModule(), "Profile");
        contentPanel.add(new ProgramModule(), "Programs");
        contentPanel.add(new SessionModule(), "Sessions");

        add(contentPanel, BorderLayout.CENTER);
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
