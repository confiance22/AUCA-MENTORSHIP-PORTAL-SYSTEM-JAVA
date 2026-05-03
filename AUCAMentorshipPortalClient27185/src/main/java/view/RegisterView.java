package view;

import controller.RegisterController;

import javax.swing.*;
import java.awt.*;

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
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        new RegisterController(this);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 8, 7, 8);

        // Title
        JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(40, 167, 69));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel subTitle = new JLabel("Fill in your details to register", SwingConstants.CENTER);
        subTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(subTitle, gbc);

        gbc.gridwidth = 1;

        // First Name
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("First Name:"), gbc);
        firstNameField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Last Name:"), gbc);
        lastNameField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Phone Number:"), gbc);
        phoneField = new JTextField(18);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Register as:"), gbc);
        roleComboBox = new JComboBox<>(new String[]{"MENTOR", "MENTEE"});
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 8;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        confirmPasswordField = new JPasswordField(18);
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(200, 38));
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerButton, gbc);

        // Back to Login
        backToLoginButton = new JButton("← Back to Login");
        backToLoginButton.setBackground(Color.WHITE);
        backToLoginButton.setForeground(new Color(0, 122, 204));
        backToLoginButton.setFocusPainted(false);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 10;
        mainPanel.add(backToLoginButton, gbc);

        add(mainPanel);
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
