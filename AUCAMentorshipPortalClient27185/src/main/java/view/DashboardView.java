package view;

import model.User;
import util.UITheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JButton activeNavButton = null;
    private JLabel pageTitle;
    
    // Theme colors based on role
    private Color sidebarColor;
    private Color headerColor;
    private Color roleAccentColor;
    private final Color ACCENT_COLOR = new Color(255, 255, 255, 40);

    public DashboardView(User user) {
        this.currentUser = user;
        setThemeByRole();
        
        setTitle("AUCA Mentorship Portal - " + currentUser.getRole());
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void setThemeByRole() {
        roleAccentColor = UITheme.getColorForRole(currentUser.getRole());
        sidebarColor = UITheme.getSidebarBackgroundForRole(currentUser.getRole());
        headerColor = roleAccentColor.brighter();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(sidebarColor);
        sidebarPanel.setPreferredSize(new Dimension(260, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UITheme.BORDER_COLOR));

        // Sidebar Header (User Profile Info)
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(sidebarColor);
        profilePanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        profilePanel.setMaximumSize(new Dimension(260, 150));

        JLabel nameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(UITheme.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel(currentUser.getRole().toString());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(UITheme.TEXT_MUTED);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        profilePanel.add(nameLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        profilePanel.add(roleLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(220, 1));
        sep.setForeground(UITheme.BORDER_COLOR);
        profilePanel.add(sep);

        sidebarPanel.add(profilePanel);

        // --- CONTENT AREA ---
        contentPanel = new JPanel(new CardLayout());

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
        topHeader.setPreferredSize(new Dimension(0, 70));
        topHeader.setBackground(UITheme.BACKGROUND_CARD);
        topHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR));

        pageTitle = new JLabel("Dashboard Overview");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
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
        String iconType = "Default";
        if (text.contains("Logout")) {
            iconType = "Logout";
        } else if (text.contains("Profile")) {
            iconType = "Profile";
        } else if (text.contains("Users") || text.contains("User Management")) {
            iconType = "Profile";
        } else if (text.contains("Programs")) {
            iconType = "Programs";
        } else if (text.contains("Sessions")) {
            iconType = "Sessions";
        } else if (text.contains("Reports")) {
            iconType = "Reports";
        } else if (text.contains("Notifications") || text.contains("Alerts") || text.contains("System Alerts")) {
            iconType = "Notifications";
        }

        JButton btn = new JButton(text);
        btn.setIcon(new CustomSidebarIcon(iconType));
        btn.setIconTextGap(15);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(UITheme.TEXT_MUTED);
        btn.setBackground(sidebarColor);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeNavButton) {
                    btn.setBackground(new Color(255, 255, 255, 15));
                    btn.setForeground(UITheme.TEXT_PRIMARY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeNavButton) {
                    btn.setBackground(sidebarColor);
                    btn.setForeground(UITheme.TEXT_MUTED);
                }
            }
        });

        if (moduleName != null) {
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, moduleName);
                pageTitle.setText(text);
                setActiveButton(btn);
            });
        }

        return btn;
    }

    private void setActiveButton(JButton btn) {
        if (activeNavButton != null) {
            activeNavButton.setBackground(sidebarColor);
            activeNavButton.setForeground(UITheme.TEXT_MUTED);
            activeNavButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        }
        activeNavButton = btn;
        activeNavButton.setBackground(new Color(255, 255, 255, 25));
        activeNavButton.setForeground(UITheme.TEXT_PRIMARY);
        activeNavButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, roleAccentColor),
                new EmptyBorder(12, 16, 12, 20)
        ));
    }

    // --- 100% PLATFORM-SAFE VECTOR SIDEBAR ICON PAINTER ---
    private static class CustomSidebarIcon implements Icon {
        private final String type;

        public CustomSidebarIcon(String type) {
            this.type = type;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Vector stroke color automatically matches button's active text color!
            g2.setColor(c.getForeground());
            
            int w = getIconWidth();
            int h = getIconHeight();
            g2.translate(x, y + 2); // Center vertically with font baseline

            if (type.equals("Profile")) {
                // Draw a sleek user silhouette
                g2.fillOval(6, 0, 6, 6);
                g2.fillArc(2, 7, 14, 10, 0, 180);
            } else if (type.equals("Programs")) {
                // Draw dynamic list/menu items
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(2, 2, 16, 2);
                g2.drawLine(2, 7, 16, 7);
                g2.drawLine(2, 12, 16, 12);
            } else if (type.equals("Sessions")) {
                // Draw a sleek calendar or clock representation
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawOval(2, 1, 14, 14);
                g2.drawLine(9, 3, 9, 8);
                g2.drawLine(9, 8, 13, 8);
            } else if (type.equals("Reports")) {
                // Draw a clean document layout
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(2, 1, 14, 14, 2, 2);
                g2.drawLine(5, 5, 13, 5);
                g2.drawLine(5, 9, 13, 9);
            } else if (type.equals("Notifications")) {
                // Draw an elegant envelope
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(2, 2, 14, 11, 2, 2);
                g2.drawLine(2, 2, 9, 8);
                g2.drawLine(16, 2, 9, 8);
            } else if (type.equals("Logout")) {
                // Draw an exit arrow
                g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(2, 8, 16, 8);
                g2.drawLine(11, 3, 16, 8);
                g2.drawLine(11, 13, 16, 8);
            } else {
                // Sleek central diamond bullet
                g2.translate(6, 4);
                g2.rotate(Math.toRadians(45));
                g2.fillRect(0, 0, 6, 6);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }
}
