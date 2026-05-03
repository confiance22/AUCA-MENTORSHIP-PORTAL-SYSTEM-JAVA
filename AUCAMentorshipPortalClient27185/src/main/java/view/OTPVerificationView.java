package view;

import javax.swing.*;
import java.awt.*;

public class OTPVerificationView extends JFrame {

    private JTextField otpField;
    private JButton verifyButton;

    public OTPVerificationView() {
        setTitle("AUCA Mentorship Portal - OTP Verification");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
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

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("OTP:"), gbc);

        otpField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(otpField, gbc);

        verifyButton = new JButton("Verify");
        verifyButton.setBackground(new Color(0, 122, 204));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFocusPainted(false);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(verifyButton, gbc);

        add(mainPanel);
    }

    public JTextField getOtpField() {
        return otpField;
    }

    public JButton getVerifyButton() {
        return verifyButton;
    }
}
