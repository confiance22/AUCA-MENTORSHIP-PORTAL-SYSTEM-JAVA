package view;

import model.User;
import model.Notification;
import util.ServiceRegistry;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;

public class OTPVerificationView extends JFrame {

    private User userToVerify;
    private boolean isLoginFlow;
    private JTextField otpField;
    private JButton verifyButton;

    public OTPVerificationView(User user, boolean isLoginFlow) {
        this.userToVerify = user;
        this.isLoginFlow = isLoginFlow;
        setTitle("OTP Verification");
        setSize(480, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initComponents();
        
        verifyButton.addActionListener(e -> verifyOtp());
    }

    private void initComponents() {
        // A. Background Canvas with deep solid dark blue and subtle ambient backlight center spot
        JPanel wrapper = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                
                // Solid deep midnight dark blue background
                g2.setColor(new Color(10, 14, 26)); 
                g2.fillRect(0, 0, w, h);
                
                // Dark Blue spotlight glow
                float[] fractions = {0.0f, 1.0f};
                Color[] radialColors = {
                    new Color(29, 78, 216, 10), 
                    new Color(0, 0, 0, 0)
                };
                g2.setPaint(new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(w / 2f, h / 2f),
                    w * 0.7f, fractions, radialColors
                ));
                g2.fillRect(0, 0, w, h);
                
                g2.dispose();
            }
        };
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        // B. Frosted Glassmorphism Card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle ambient dark blue glass glow backing
                g2.setColor(new Color(29, 78, 216, 6));
                g2.fillRoundRect(-10, -10, getWidth() + 20, getHeight() + 20, 28, 28);
                
                // Dark Card surface
                g2.setColor(new Color(13, 17, 23, 220)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Clean reflecting dark blue border
                g2.setColor(new Color(30, 58, 138, 150)); // Deep dark blue border (#1E3A8A)
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                // Translucent white stroke overlay
                g2.setColor(new Color(255, 255, 255, 15));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(35, 30, 35, 30));
        card.setPreferredSize(new Dimension(360, 420));

        // Stylized dark blue lock logo
        JPanel lockBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                // Outer circle
                g2.setColor(new Color(29, 78, 216, 20));
                g2.fillOval(0, 0, w, h);
                g2.setColor(new Color(29, 78, 216, 100));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, w - 1, h - 1);
                
                // Stylized secure keyhole arc in dark blue
                g2.setColor(new Color(29, 78, 216));
                g2.fillRoundRect(w / 2 - 6, h / 2 - 2, 12, 14, 3, 3);
                g2.drawArc(w / 2 - 8, h / 2 - 12, 16, 14, 0, 180);
                g2.dispose();
            }
        };
        lockBadge.setPreferredSize(new Dimension(50, 50));
        lockBadge.setMaximumSize(new Dimension(50, 50));
        lockBadge.setOpaque(false);
        lockBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Verify Identity", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(15, 0, 8, 0));

        JLabel infoLabel = new JLabel("<html><center>We've sent a 6-digit secure code to:<br><font color='#1D4ED8'><b>" + userToVerify.getEmail() + "</b></font></center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoLabel.setForeground(new Color(156, 163, 175));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        otpField = new JTextField();
        otpField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        otpField.setHorizontalAlignment(JTextField.CENTER);
        otpField.setMaximumSize(new Dimension(240, 52));
        otpField.setPreferredSize(new Dimension(240, 52));
        otpField.setAlignmentX(Component.CENTER_ALIGNMENT);
        otpField.putClientProperty(FlatClientProperties.STYLE, 
            "background: #0F1219; " +
            "foreground: #1D4ED8; " + // Dark Blue text color
            "arc: 100; " +
            "borderWidth: 1; " +
            "borderColor: #1F2937; " +
            "focusColor: #1D4ED8; " +
            "focusedBorderColor: #1D4ED8; " +
            "caretColor: #1D4ED8"
        );

        verifyButton = new JButton(isLoginFlow ? "Verify & Login" : "Verify & Activate");
        verifyButton.setMaximumSize(new Dimension(240, 42));
        verifyButton.setPreferredSize(new Dimension(240, 42));
        verifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        verifyButton.putClientProperty(FlatClientProperties.STYLE, 
            "background: #1E3A8A; " + // Deep Dark blue button background
            "foreground: #FFFFFF; " +
            "arc: 100; " +
            "hoverBackground: #1D4ED8; " +
            "focusedBackground: #1E3A8A; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );

        card.add(lockBadge);
        card.add(titleLabel);
        card.add(infoLabel);
        card.add(otpField);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
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
                if (!isLoginFlow) {
                    userToVerify.setIsActive(true);
                    ServiceRegistry.userService.updateUserRecord(userToVerify);
                    ServiceRegistry.notificationService.notifyAdmins("New User Activated: " + userToVerify.getFirstName() + " (" + userToVerify.getRole() + ")");
                }
                
                notif.setUsed(true);
                ServiceRegistry.notificationService.updateNotificationRecord(notif);

                if (isLoginFlow) {
                    MessageDialogUtil.showSuccess(this, "Login verified successfully! Welcome " + userToVerify.getFirstName());
                    this.dispose();
                    new DashboardView(userToVerify).setVisible(true);
                } else {
                    MessageDialogUtil.showSuccess(this, "Account activated successfully! You can now log in.");
                    this.dispose();
                    new LoginView().setVisible(true);
                }
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
