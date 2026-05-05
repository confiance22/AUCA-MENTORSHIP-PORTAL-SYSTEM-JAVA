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

    // Consistency Colors
    private final Color PRIMARY_COLOR = new Color(30, 58, 138); 
    private final Color SUCCESS_COLOR = new Color(5, 150, 105); 
    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color TEXT_COLOR = new Color(17, 24, 39);

    public RegisterView() {
        setTitle("AUCA Mentorship Portal - Create Account");
        setSize(520, 720); // Slightly taller for more fields
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

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(25, 35, 30, 35)
        ));

        // Header
        JLabel titleLabel = new JLabel("Join the Community", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Experience mentorship like never before", SwingConstants.CENTER);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subTitle.setBorder(new EmptyBorder(5, 0, 25, 0));

        // Assembly
        card.add(titleLabel);
        card.add(subTitle);

        // Fields in Grid (Name Row)
        JPanel namePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        namePanel.setBackground(Color.WHITE);
        namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        
        firstNameField = createField("First Name", namePanel);
        lastNameField = createField("Last Name", namePanel);
        card.add(namePanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        emailField = createFullField("Email Address", card);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        phoneField = createFullField("Phone Number", card);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Role Selector
        card.add(createLabel("I want to join as:"));
        roleComboBox = new JComboBox<>(new String[]{"MENTOR", "MENTEE"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(roleComboBox);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        passwordField = createFullPasswordField("Password", card);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        confirmPasswordField = createFullPasswordField("Confirm Password", card);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons
        registerButton = new JButton("Create My Account");
        stylePrimaryButton(registerButton);
        card.add(registerButton);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        backToLoginButton = new JButton("Already have an account? Sign In");
        styleSecondaryButton(backToLoginButton);
        card.add(backToLoginButton);

        wrapper.add(card);
        add(new JScrollPane(wrapper)); // Scrollable just in case
    }

    private JTextField createField(String labelText, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.add(createLabel(labelText), BorderLayout.NORTH);
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(100, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        p.add(f, BorderLayout.CENTER);
        parent.add(p);
        return f;
    }

    private JTextField createFullField(String labelText, JPanel parent) {
        parent.add(createLabel(labelText));
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setPreferredSize(new Dimension(300, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        parent.add(f);
        return f;
    }

    private JPasswordField createFullPasswordField(String labelText, JPanel parent) {
        parent.add(createLabel(labelText));
        JPasswordField f = new JPasswordField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setPreferredSize(new Dimension(300, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        parent.add(f);
        return f;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(75, 85, 99));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(5, 0, 3, 0));
        return label;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(SUCCESS_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
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
