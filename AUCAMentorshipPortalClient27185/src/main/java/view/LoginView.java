package view;

import controller.LoginController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Define consistent colors
    private final Color PRIMARY_COLOR = new Color(30, 58, 138); // Deep AUCA Blue
    private final Color ACCENT_COLOR = new Color(59, 130, 246);  // Light Blue
    private final Color BG_COLOR = new Color(243, 244, 246);      // Soft Gray
    private final Color TEXT_COLOR = new Color(17, 24, 39);      // Dark Charcoal

    public LoginView() {
        setTitle("AUCA Mentorship Portal - Secure Login");
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Use a clean layout
        getContentPane().setBackground(BG_COLOR);
        initComponents();
        
        new LoginController(this);
    }

    private void initComponents() {
        // Main Container with padding
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Login Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(30, 40, 40, 40)
        ));
        
        // Header
        JLabel logoText = new JLabel("AUCA", SwingConstants.CENTER);
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoText.setForeground(PRIMARY_COLOR);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Mentorship Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Welcome back! Please sign in", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitle.setBorder(new EmptyBorder(5, 0, 30, 0));

        // Form Fields
        emailField = createStyledTextField("Email Address");
        passwordField = createStyledPasswordField("Password");
        
        // Buttons
        loginButton = new JButton("Sign In");
        stylePrimaryButton(loginButton);
        
        registerButton = new JButton("Create New Account");
        styleSecondaryButton(registerButton);

        // Assembly
        card.add(logoText);
        card.add(titleLabel);
        card.add(subTitle);
        
        card.add(createLabel("Email Address"));
        card.add(emailField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        card.add(createLabel("Password"));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        
        card.add(loginButton);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(registerButton);

        wrapper.add(card);
        add(wrapper);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(75, 85, 99));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getRegisterButton() { return registerButton; }
}
