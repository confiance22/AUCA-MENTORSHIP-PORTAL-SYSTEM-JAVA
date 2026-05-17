package view;

import controller.LoginController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        setTitle("AUCA Mentorship Portal - Login");
        setSize(1020, 620); // Spacious width to prevent collision
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();

        new LoginController(this);
    }

    private void initComponents() {
        // --- 1. SOLID DARK BLUE BACKGROUND CANVAS ---
        JPanel wrapper = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                
                // Pure deep dark navy blue solid background
                g2.setColor(new Color(10, 14, 26)); // #0A0E1A Deep Midnight Dark Blue
                g2.fillRect(0, 0, w, h);
                
                // Faint elegant dark blue ambient backlight center spot
                float[] fractions = {0.0f, 1.0f};
                Color[] radialColors = {
                    new Color(37, 99, 235, 10), // Soft dark blue glow
                    new Color(0, 0, 0, 0)
                };
                g2.setPaint(new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(w * 0.75f, h * 0.75f),
                    w * 0.5f, fractions, radialColors
                ));
                g2.fillRect(0, 0, w, h);
                
                g2.dispose();
            }
        };
        wrapper.setBackground(new Color(10, 14, 26));

        // --- 2. LEFT SIDE: WELCOME HERO PANEL (AUCA SPECIFIC) ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(50, 80, 50, 40));

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0;
        gbcL.weightx = 1.0;
        gbcL.fill = GridBagConstraints.HORIZONTAL;
        gbcL.anchor = GridBagConstraints.WEST;

        // Stylized dark blue logo block
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(37, 99, 235)); // Solid dark blue logo (#2563EB)
                g2.fillRoundRect(0, 0, 12, 28, 4, 4);
                g2.fillRoundRect(16, 8, 12, 20, 4, 4);
                g2.dispose();
            }
        };
        logoPanel.setPreferredSize(new Dimension(35, 30));
        logoPanel.setOpaque(false);
        gbcL.gridy = 0;
        gbcL.insets = new Insets(0, 0, 35, 0);
        leftPanel.add(logoPanel, gbcL);

        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 54));
        welcomeLabel.setForeground(Color.WHITE);
        gbcL.gridy = 1;
        gbcL.insets = new Insets(0, 0, 12, 0);
        leftPanel.add(welcomeLabel, gbcL);

        // Dark Blue Underline bar
        JPanel bar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(30, 58, 138)); // Deep Dark Blue bar (#1E3A8A)
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setPreferredSize(new Dimension(50, 3));
        bar.setMaximumSize(new Dimension(50, 3));
        bar.setOpaque(false);
        gbcL.gridy = 2;
        gbcL.insets = new Insets(0, 0, 30, 0);
        leftPanel.add(bar, gbcL);

        // AUCA Mentorship specific content
        JLabel descriptionLabel = new JLabel("<html>Built by students, for students. Find your ideal mentor, share<br>" +
            "academic guidance, and grow together at AUCA.</html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionLabel.setForeground(new Color(156, 163, 175)); // #9CA3AF
        gbcL.gridy = 3;
        gbcL.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(descriptionLabel, gbcL);

        // --- 3. RIGHT SIDE: FORM CONTAINER (GLASSMORPHISM) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(50, 40, 50, 80));

        // Floating Frosted Glass Card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle ambient dark blue glass glow backing
                g2.setColor(new Color(37, 99, 235, 6));
                g2.fillRoundRect(-10, -10, getWidth() + 20, getHeight() + 20, 28, 28);
                
                // Dark Card surface
                g2.setColor(new Color(13, 17, 23, 220)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                // Clean reflecting dark blue border
                g2.setColor(new Color(30, 58, 138, 150)); // Deep dark blue border (#1E3A8A)
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                
                // Overlap reflection line
                g2.setColor(new Color(255, 255, 255, 15));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(360, 400));
        card.setBorder(new EmptyBorder(35, 30, 30, 30));

        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.gridx = 0;
        gbcC.weightx = 1.0;

        // Card Title
        JLabel cardTitle = new JLabel("Sign in", SwingConstants.CENTER);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        cardTitle.setForeground(Color.WHITE);
        gbcC.gridy = 0;
        gbcC.insets = new Insets(0, 0, 25, 0);
        card.add(cardTitle, gbcC);

        // Fields: User Name
        JLabel uNameLabel = new JLabel("User Name");
        uNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        uNameLabel.setForeground(new Color(156, 163, 175));
        gbcC.gridy = 1;
        gbcC.insets = new Insets(0, 0, 4, 0);
        card.add(uNameLabel, gbcC);

        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(300, 38));
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        emailField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        emailField.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " + // Deep dark blue focused color
            "focusedBorderColor: #1D4ED8"
        );
        JLabel mailIcon = new JLabel("  ✉  ");
        mailIcon.setForeground(new Color(156, 163, 175));
        emailField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, mailIcon);

        gbcC.gridy = 2;
        gbcC.insets = new Insets(0, 0, 12, 0);
        card.add(emailField, gbcC);

        // Fields: Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(156, 163, 175));
        gbcC.gridy = 3;
        gbcC.insets = new Insets(0, 0, 4, 0);
        card.add(passLabel, gbcC);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 38));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        passwordField.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8"
        );
        JLabel passIcon = new JLabel("  🔒  ");
        passIcon.setForeground(new Color(156, 163, 175));
        passwordField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, passIcon);

        gbcC.gridy = 4;
        gbcC.insets = new Insets(0, 0, 25, 0);
        card.add(passwordField, gbcC);

        // Primary Submit Button (Dark Blue Gradient)
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E3A8A; " + // Deep Dark blue button background (#1E3A8A)
            "foreground: #FFFFFF; " +
            "arc: 100; " +
            "hoverBackground: #1D4ED8; " +
            "focusedBackground: #1E3A8A; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        gbcC.gridy = 5;
        gbcC.insets = new Insets(0, 0, 20, 0);
        card.add(loginButton, gbcC);

        // Bottom link to register (Dark Blue Accent)
        registerButton = new JButton("New here? Create an Account");
        registerButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerButton.setForeground(new Color(37, 99, 235)); // Soft dark blue accent
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setText("<html><u>New here? Create an Account</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setText("New here? Create an Account");
            }
        });

        gbcC.gridy = 6;
        gbcC.insets = new Insets(0, 0, 0, 0);
        card.add(registerButton, gbcC);

        rightPanel.add(card);

        wrapper.add(leftPanel);
        wrapper.add(rightPanel);
        add(wrapper);
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}
