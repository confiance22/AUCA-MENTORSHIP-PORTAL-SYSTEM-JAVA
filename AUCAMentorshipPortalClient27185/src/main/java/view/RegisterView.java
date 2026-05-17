package view;

import controller.RegisterController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterView extends JFrame {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton;
    private JButton backToLoginButton;

    public RegisterView() {
        setTitle("AUCA Mentorship Portal - Register");
        setSize(1020, 700); // Spacious width to prevent collision
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
        
        new RegisterController(this);
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

        JLabel welcomeLabel = new JLabel("Join Us!");
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
        JLabel descriptionLabel = new JLabel("<html>Connect with peers, learn from seniors, and share the journey.<br>" +
            "Join our student-led AUCA mentorship community.</html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionLabel.setForeground(new Color(156, 163, 175)); // #9CA3AF
        gbcL.gridy = 3;
        gbcL.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(descriptionLabel, gbcL);

        // --- 3. RIGHT SIDE: FORM CONTAINER (GLASSMORPHISM) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 80));

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
        card.setPreferredSize(new Dimension(380, 540));
        card.setBorder(new EmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.gridx = 0;
        gbcC.weightx = 1.0;

        // Title
        JLabel cardTitle = new JLabel("Sign up", SwingConstants.CENTER);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        cardTitle.setForeground(Color.WHITE);
        gbcC.gridy = 0;
        gbcC.insets = new Insets(0, 0, 20, 0);
        card.add(cardTitle, gbcC);

        // Name Grid Row (First Name and Last Name)
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 12, 0));
        namePanel.setOpaque(false);
        firstNameField = createFieldInPanel("First Name", namePanel, "  👤  ");
        lastNameField = createFieldInPanel("Last Name", namePanel, "  👤  ");
        
        gbcC.gridy = 1;
        gbcC.insets = new Insets(0, 0, 10, 0);
        card.add(namePanel, gbcC);

        // Email address
        JLabel mailLabel = new JLabel("Email Address");
        mailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        mailLabel.setForeground(new Color(156, 163, 175));
        gbcC.gridy = 2;
        gbcC.insets = new Insets(0, 0, 4, 0);
        card.add(mailLabel, gbcC);

        emailField = createStyledTextField("  ✉  ");
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        gbcC.gridy = 3;
        gbcC.insets = new Insets(0, 0, 10, 0);
        card.add(emailField, gbcC);

        // Phone number
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        phoneLabel.setForeground(new Color(156, 163, 175));
        gbcC.gridy = 4;
        gbcC.insets = new Insets(0, 0, 4, 0);
        card.add(phoneLabel, gbcC);

        phoneField = createStyledTextField("  📞  ");
        phoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your phone");
        gbcC.gridy = 5;
        gbcC.insets = new Insets(0, 0, 10, 0);
        card.add(phoneField, gbcC);

        // User role
        JLabel roleLabel = new JLabel("I am joining as:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roleLabel.setForeground(new Color(156, 163, 175));
        gbcC.gridy = 6;
        gbcC.insets = new Insets(0, 0, 4, 0);
        card.add(roleLabel, gbcC);

        roleComboBox = new JComboBox<>(new String[]{"MENTOR", "MENTEE"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleComboBox.setPreferredSize(new Dimension(0, 36));
        roleComboBox.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8"
        );
        gbcC.gridy = 7;
        gbcC.insets = new Insets(0, 0, 10, 0);
        card.add(roleComboBox, gbcC);

        // Password grid row (Password and Confirm)
        JPanel passPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        passPanel.setOpaque(false);
        passwordField = createPasswordFieldInPanel("Password", passPanel);
        confirmPasswordField = createPasswordFieldInPanel("Confirm", passPanel);
        
        gbcC.gridy = 8;
        gbcC.insets = new Insets(0, 0, 20, 0);
        card.add(passPanel, gbcC);

        // Register Button (Dark Blue Background)
        registerButton = new JButton("Create My Account");
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E3A8A; " + // Deep Dark blue button background (#1E3A8A)
            "foreground: #FFFFFF; " +
            "arc: 100; " +
            "hoverBackground: #1D4ED8; " +
            "focusedBackground: #1E3A8A; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        gbcC.gridy = 9;
        gbcC.insets = new Insets(0, 0, 18, 0);
        card.add(registerButton, gbcC);

        // Back to Login link (Dark Blue Accent)
        backToLoginButton = new JButton("Already have an account? Sign In");
        backToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backToLoginButton.setForeground(new Color(37, 99, 235)); // Soft dark blue accent
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backToLoginButton.setText("<html><u>Already have an account? Sign In</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backToLoginButton.setText("Already have an account? Sign In");
            }
        });

        gbcC.gridy = 10;
        gbcC.insets = new Insets(0, 0, 0, 0);
        card.add(backToLoginButton, gbcC);

        rightPanel.add(card);

        wrapper.add(leftPanel);
        wrapper.add(rightPanel);
        add(wrapper);
    }

    private JTextField createFieldInPanel(String labelText, JPanel parent, String leadingIcon) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(156, 163, 175));
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        p.add(lbl, BorderLayout.NORTH);
        
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(100, 36));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, labelText);
        f.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        f.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8"
        );
        
        JLabel icon = new JLabel(leadingIcon);
        icon.setForeground(new Color(156, 163, 175));
        f.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, icon);
        
        p.add(f, BorderLayout.CENTER);
        parent.add(p);
        return f;
    }

    private JPasswordField createPasswordFieldInPanel(String labelText, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(156, 163, 175));
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        p.add(lbl, BorderLayout.NORTH);
        
        JPasswordField f = new JPasswordField();
        f.setPreferredSize(new Dimension(100, 36));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, labelText);
        f.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        f.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8"
        );
        
        JLabel icon = new JLabel("  🔒  ");
        icon.setForeground(new Color(156, 163, 175));
        f.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, icon);
        
        p.add(f, BorderLayout.CENTER);
        parent.add(p);
        return f;
    }

    private JTextField createStyledTextField(String leadingIcon) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 36));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        field.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #F3F4F6; " +
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8"
        );
        
        JLabel icon = new JLabel(leadingIcon);
        icon.setForeground(new Color(156, 163, 175));
        field.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, icon);
        return field;
    }

    public JTextField getFirstNameField() { return firstNameField; }
    public JTextField getLastNameField() { return lastNameField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getPhoneField() { return phoneField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JPasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public JComboBox<String> getRoleComboBox() { return roleComboBox; }
    public JButton getRegisterButton() { return registerButton; }
    public JButton getBackToLoginButton() { return backToLoginButton; }
}
