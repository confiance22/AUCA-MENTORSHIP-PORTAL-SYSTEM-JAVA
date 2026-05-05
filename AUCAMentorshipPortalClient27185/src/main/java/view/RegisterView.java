package view;

import controller.RegisterController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private final Color PRIMARY_COLOR = new Color(30, 58, 138); 
    private final Color SUCCESS_COLOR = new Color(5, 150, 105); 
    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color TEXT_COLOR = new Color(17, 24, 39);

    public RegisterView() {
        setTitle("AUCA Mentorship Portal - Create Account");
        setSize(800, 850); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(BG_COLOR);
        initComponents();
        
        new RegisterController(this);
    }

    private void initComponents() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- THE "INSIDE" CARD ---
        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(600, 750)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. Header
        JLabel titleLabel = new JLabel("Join the Community", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(titleLabel, gbc);
        
        JLabel subTitle = new JLabel("Sign up to start your mentorship journey", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(subTitle, gbc);

        // 2. Name Row (Two columns)
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 15, 0));
        namePanel.setBackground(Color.WHITE);
        
        firstNameField = createFieldInPanel("First Name", namePanel);
        lastNameField = createFieldInPanel("Last Name", namePanel);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(namePanel, gbc);

        // 3. Email
        card.add(createLabel("Email Address"), createGbc(3));
        emailField = createStyledTextField();
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(emailField, gbc);

        // 4. Phone
        card.add(createLabel("Phone Number"), createGbc(5));
        phoneField = createStyledTextField();
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(phoneField, gbc);

        // 5. Role
        card.add(createLabel("I am joining as:"), createGbc(7));
        roleComboBox = new JComboBox<>(new String[]{"MENTOR", "MENTEE"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(roleComboBox, gbc);

        // 6. Password Row
        JPanel passPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        passPanel.setBackground(Color.WHITE);
        passwordField = createPasswordFieldInPanel("Password", passPanel);
        confirmPasswordField = createPasswordFieldInPanel("Confirm Password", passPanel);
        
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(passPanel, gbc);

        // 7. Buttons
        registerButton = new JButton("Create My Account");
        stylePrimaryButton(registerButton);
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(registerButton, gbc);
        
        backToLoginButton = new JButton("Already have an account? Sign In");
        styleSecondaryButton(backToLoginButton);
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(backToLoginButton, gbc);

        wrapper.add(card);
        add(wrapper); // Removed JScrollPane
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

    private JTextField createFieldInPanel(String labelText, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.add(createLabel(labelText), BorderLayout.NORTH);
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(100, 42));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        p.add(f, BorderLayout.CENTER);
        parent.add(p);
        return f;
    }

    private JPasswordField createPasswordFieldInPanel(String labelText, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.add(createLabel(labelText), BorderLayout.NORTH);
        JPasswordField f = new JPasswordField();
        f.setPreferredSize(new Dimension(100, 42));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        p.add(f, BorderLayout.CENTER);
        parent.add(p);
        return f;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(300, 42));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 12, 5, 12)
        ));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(75, 85, 99));
        return label;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setPreferredSize(new Dimension(380, 50));
        btn.setBackground(SUCCESS_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setPreferredSize(new Dimension(380, 45));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
