package view;

import model.User;
import model.Notification;
import util.ServiceRegistry;
import javax.swing.*;
import java.awt.*;

public class OTPVerificationView extends JFrame {

    private User userToVerify;
    private JTextField otpField;
    private JButton verifyButton;

    public OTPVerificationView(User user) {
        this.userToVerify = user;
        setTitle("AUCA Mentorship Portal - OTP Verification");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        
        verifyButton.addActionListener(e -> verifyOtp());
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Enter OTP Code", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel infoLabel = new JLabel("Code sent to: " + userToVerify.getEmail(), SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 1;
        mainPanel.add(infoLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("OTP:"), gbc);

        otpField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(otpField, gbc);

        verifyButton = new JButton("Verify");
        verifyButton.setBackground(new Color(0, 122, 204));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFocusPainted(false);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(verifyButton, gbc);

        add(mainPanel);
    }

    private void verifyOtp() {
        String code = otpField.getText().trim();
        if (code.isEmpty()) return;

        try {
            Notification notif = ServiceRegistry.notificationService.findValidOtpRecord(userToVerify.getId(), code);
            if (notif != null) {
                // OTP is correct! Activate user
                userToVerify.setIsActive(true);
                ServiceRegistry.userService.updateUserRecord(userToVerify);
                
                // Mark OTP as used
                notif.setUsed(true);
                ServiceRegistry.notificationService.updateNotificationRecord(notif);

                JOptionPane.showMessageDialog(this, "Account activated successfully! You can now log in.");
                this.dispose();
                new LoginView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid or expired OTP code. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection error: " + ex.getMessage());
        }
    }

    public JTextField getOtpField() {
        return otpField;
    }

    public JButton getVerifyButton() {
        return verifyButton;
    }
}
