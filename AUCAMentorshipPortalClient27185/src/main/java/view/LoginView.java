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

    private final Color PRIMARY_COLOR = new Color(30, 58, 138); 
    private final Color BG_COLOR = new Color(243, 244, 246);      
    private final Color TEXT_COLOR = new Color(17, 24, 39);      

    public LoginView() {
        setTitle("AUCA Mentorship Portal - Secure Login");
        setSize(700, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(BG_COLOR);
        initComponents();
        
        new LoginController(this);
    }

    private void initComponents() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(500, 620)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(40, 60, 40, 60)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. Logo
        JLabel logoText = new JLabel("AUCA", SwingConstants.CENTER);
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logoText.setForeground(PRIMARY_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(logoText, gbc);
        
        // 2. Title
        JLabel titleLabel = new JLabel("Mentorship Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(titleLabel, gbc);
        
        // 3. Subtitle
        JLabel subTitle = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(Color.GRAY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        card.add(subTitle, gbc);

        // 4. Fields
        card.add(createLabel("Email Address"), createGbc(3));
        emailField = createStyledTextField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(emailField, gbc);
        
        card.add(createLabel("Password"), createGbc(5));
        passwordField = createStyledPasswordField();
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 35, 0);
        card.add(passwordField, gbc);
        
        // 5. Sign In
        loginButton = new JButton("Sign In");
        stylePrimaryButton(loginButton);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(loginButton, gbc);

        // 6. Separator
        JLabel orLabel = new JLabel("────  Don't have an account?  ────", SwingConstants.CENTER);
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orLabel.setForeground(Color.GRAY);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(orLabel, gbc);
        
        // 7. Register
        registerButton = new JButton("Create New Account");
        styleSecondaryButton(registerButton);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(registerButton, gbc);

        wrapper.add(card);
        add(wrapper);
    }

    private GridBagConstraints createGbc(int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        return gbc;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(75, 85, 99));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(380, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(380, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        return field;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setPreferredSize(new Dimension(380, 50));
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setPreferredSize(new Dimension(380, 50));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getRegisterButton() { return registerButton; }
}
