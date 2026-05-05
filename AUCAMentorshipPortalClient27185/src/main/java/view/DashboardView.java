package view;

import model.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    
    // Theme colors based on role
    private Color sidebarColor;
    private Color headerColor;
    private final Color ACCENT_COLOR = new Color(255, 255, 255, 40);

    public DashboardView(User user) {
        this.currentUser = user;
        setThemeByRole();
        
        setTitle("AUCA Mentorship Portal - " + currentUser.getRole());
        setSize(1100, 750); // Increased size for a more professional feel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void setThemeByRole() {
        switch (currentUser.getRole()) {
            case ADMIN:
                sidebarColor = new Color(15, 23, 42); // Midnight Blue
                headerColor = new Color(30, 41, 59);
                break;
            case MENTOR:
                sidebarColor = new Color(6, 78, 59); // Forest Green
                headerColor = new Color(5, 150, 105);
                break;
            case MENTEE:
                sidebarColor = new Color(30, 58, 138); // Royal Blue
                headerColor = new Color(59, 130, 246);
                break;
            default:
                sidebarColor = new Color(55, 65, 81);
                headerColor = new Color(107, 114, 128);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(sidebarColor);
        sidebarPanel.setPreferredSize(new Dimension(260, 0));
        sidebarPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Sidebar Header (User Profile Info)
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(sidebarColor);
        profilePanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        profilePanel.setMaximumSize(new Dimension(260, 150));

        JLabel nameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel(currentUser.getRole().toString());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(209, 213, 219));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        profilePanel.add(nameLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        profilePanel.add(roleLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(220, 1));
        sep.setForeground(new Color(255, 255, 255, 50));
        profilePanel.add(sep);

        sidebarPanel.add(profilePanel);

        // --- CONTENT AREA ---
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);

        // Add modules based on role
        setupNavigation();

        // Footer / Logout
        sidebarPanel.add(Box.createVerticalGlue());
        JButton logoutBtn = createMenuButton("Logout", null);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        sidebarPanel.add(logoutBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- TOP HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(Color.WHITE);
        topHeader.setPreferredSize(new Dimension(0, 70));
        topHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        JLabel pageTitle = new JLabel("Dashboard Overview");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pageTitle.setBorder(new EmptyBorder(0, 30, 0, 0));
        topHeader.add(pageTitle, BorderLayout.WEST);

        // Assembler
        add(sidebarPanel, BorderLayout.WEST);
        
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.add(topHeader, BorderLayout.NORTH);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
        add(mainWrapper, BorderLayout.CENTER);
    }

    private void setupNavigation() {
        // Welcome Panel
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(Color.WHITE);
        JLabel welcomeText = new JLabel("Welcome back, " + currentUser.getFirstName() + "!");
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomePanel.add(welcomeText);
        contentPanel.add(welcomePanel, "Welcome");

        switch (currentUser.getRole()) {
            case ADMIN:
                addNavigationItem("User Management", "Users", new UserModule(currentUser));
                addNavigationItem("Mentorship Programs", "Programs", new ProgramModule(currentUser));
                addNavigationItem("System Reports", "Reports", new ReportModule(currentUser));
                addNavigationItem("System Alerts", "Notifications", new NotificationModule(currentUser));
                break;
            case MENTOR:
                addNavigationItem("My Profile", "Profile", new MentorProfileModule(currentUser));
                addNavigationItem("Programs", "Programs", new ProgramModule(currentUser));
                addNavigationItem("Sessions", "Sessions", new SessionModule(currentUser));
                addNavigationItem("Reports", "Reports", new ReportModule(currentUser));
                addNavigationItem("Notifications", "Notifications", new NotificationModule(currentUser));
                break;
            case MENTEE:
                addNavigationItem("Browse Programs", "Programs", new ProgramModule(currentUser));
                addNavigationItem("My Sessions", "Sessions", new SessionModule(currentUser));
                addNavigationItem("My Notifications", "Notifications", new NotificationModule(currentUser));
                addNavigationItem("My Profile", "Profile", new MentorProfileModule(currentUser));
                break;
        }
    }

    private void addNavigationItem(String text, String moduleName, JPanel module) {
        JButton btn = createMenuButton(text, moduleName);
        sidebarPanel.add(btn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(module, moduleName);
    }

    private JButton createMenuButton(String text, String moduleName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(229, 231, 235));
        btn.setBackground(sidebarColor);
        btn.setBorder(new EmptyBorder(12, 25, 12, 25));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(headerColor);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(sidebarColor);
                btn.setForeground(new Color(229, 231, 235));
            }
        });

        if (moduleName != null) {
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, moduleName);
            });
        }

        return btn;
    }
}
