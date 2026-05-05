package view;

import model.User;
import model.Notification;
import util.ServiceRegistry;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OTPVerificationView extends JFrame {

    private User userToVerify;
    private JTextField otpField;
    private JButton verifyButton;

    private final Color PRIMARY_COLOR = new Color(30, 58, 138); 
    private final Color BG_COLOR = new Color(243, 244, 246);      
    private final Color TEXT_COLOR = new Color(17, 24, 39);

    public OTPVerificationView(User user) {
        this.userToVerify = user;
        setTitle("AUCA Mentorship Portal - Identity Verification");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(BG_COLOR);
        initComponents();
        
        verifyButton.addActionListener(e -> verifyOtp());
    }

    private void initComponents() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(40, 40, 40, 40)
        ));

        JLabel iconLabel = new JLabel("🔒", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Verify Identity", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("<html><center>We've sent a 6-digit code to:<br><b>" + userToVerify.getEmail() + "</b></center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(new EmptyBorder(10, 0, 30, 0));

        otpField = new JTextField();
        otpField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        otpField.setHorizontalAlignment(JTextField.CENTER);
        otpField.setMaximumSize(new Dimension(250, 60));
        otpField.setPreferredSize(new Dimension(250, 60));
        otpField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            new EmptyBorder(5, 5, 5, 5)
        ));

        verifyButton = new JButton("Verify & Activate");
        verifyButton.setMaximumSize(new Dimension(250, 50));
        verifyButton.setPreferredSize(new Dimension(250, 50));
        verifyButton.setBackground(PRIMARY_COLOR);
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        verifyButton.setFocusPainted(false);
        verifyButton.setBorderPainted(false);
        verifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(infoLabel);
        card.add(otpField);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(verifyButton);

        wrapper.add(card);
        add(wrapper);
    }

    private void verifyOtp() {
        String code = otpField.getText().trim();
        if (code.isEmpty()) return;

        try {
            Notification notif = ServiceRegistry.notificationService.findValidOtpRecord(userToVerify.getId(), code);
            if (notif != null) {
                userToVerify.setIsActive(true);
                ServiceRegistry.userService.updateUserRecord(userToVerify);
                
                notif.setUsed(true);
                ServiceRegistry.notificationService.updateNotificationRecord(notif);

                ServiceRegistry.notificationService.notifyAdmins("New User Activated: " + userToVerify.getFirstName() + " (" + userToVerify.getRole() + ")");

                MessageDialogUtil.showSuccess(this, "Account activated successfully! You can now log in.");
                this.dispose();
                new LoginView().setVisible(true);
            } else {
                MessageDialogUtil.showError(this, "Invalid or expired OTP code. Please try again.");
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(this, "Connection error: " + ex.getMessage());
        }
    }

    public JTextField getOtpField() { return otpField; }
    public JButton getVerifyButton() { return verifyButton; }
}
