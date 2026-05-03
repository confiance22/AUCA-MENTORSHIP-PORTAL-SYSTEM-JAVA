package view;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        setTitle("AUCA Mentorship Portal - Login");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        new LoginController(this);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Title
        JLabel titleLabel = new JLabel("AUCA Mentorship Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 122, 204));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel subTitle = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subTitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(subTitle, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 38));

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        // Separator
        JLabel orLabel = new JLabel("────  Don't have an account?  ────", SwingConstants.CENTER);
        orLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        orLabel.setForeground(Color.GRAY);
        gbc.gridy = 5;
        mainPanel.add(orLabel, gbc);

        // Register Button
        registerButton = new JButton("Create Account");
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setPreferredSize(new Dimension(200, 35));

        gbc.gridy = 6;
        mainPanel.add(registerButton, gbc);

        add(mainPanel);
    }

    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getRegisterButton() { return registerButton; }
}
