package controller;

import model.User;
import model.UserRole;
import model.Notification;
import model.NotificationType;
import util.ServiceRegistry;
import util.MessageDialogUtil;
import view.LoginView;
import view.RegisterView;
import view.OTPVerificationView;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class RegisterController {
    private RegisterView view;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.view.getRegisterButton().addActionListener(this::handleRegister);
        this.view.getBackToLoginButton().addActionListener(e -> {
            view.dispose();
            new LoginView().setVisible(true);
        });
    }

    private void handleRegister(ActionEvent e) {
        String fName = view.getFirstNameField().getText().trim();
        String lName = view.getLastNameField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String phone = view.getPhoneField().getText().trim();
        String password = new String(view.getPasswordField().getPassword());
        String confirm = new String(view.getConfirmPasswordField().getPassword());
        String roleStr = (String) view.getRoleComboBox().getSelectedItem();

        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            MessageDialogUtil.showWarning(view, "Please fill in all required fields.");
            return;
        }

        if (!email.contains("@")) {
            MessageDialogUtil.showWarning(view, "Please enter a valid email address.");
            return;
        }

        if (!password.equals(confirm)) {
            MessageDialogUtil.showWarning(view, "Passwords do not match. Please re-enter.");
            return;
        }

        if (password.length() < 6) {
            MessageDialogUtil.showWarning(view, "Password must be at least 6 characters long.");
            return;
        }

        try {
            if (ServiceRegistry.userService.findUserRecordByEmail(email) != null) {
                MessageDialogUtil.showError(view, "An account with this email already exists.\nPlease use a different email.");
                return;
            }

            User user = new User();
            user.setFirstName(fName);
            user.setLastName(lName);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setPassword(password);
            user.setRole(UserRole.valueOf(roleStr));
            user.setIsActive(false);
            user.setCreatedAt(LocalDateTime.now());

            User registeredUser = ServiceRegistry.userService.registerUserRecord(user);
            if (registeredUser != null) {
                String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
                Notification otpNotif = new Notification();
                otpNotif.setUser(registeredUser);
                otpNotif.setMessage("Your OTP verification code is: " + otp);
                otpNotif.setOtpCode(otp);
                otpNotif.setType(NotificationType.OTP);
                otpNotif.setCreatedAt(LocalDateTime.now());
                otpNotif.setExpiresAt(LocalDateTime.now().plusMinutes(15));
                otpNotif.setUsed(false);
                otpNotif.setRead(false);
                ServiceRegistry.notificationService.registerNotificationRecord(otpNotif);

                MessageDialogUtil.showSuccess(view, "Registration successful!\nAn OTP has been sent to your email for verification.");
                view.dispose();
                new OTPVerificationView(registeredUser).setVisible(true);
            } else {
                MessageDialogUtil.showError(view, "Registration failed. Please try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialogUtil.showError(view, "Server error during registration:\n" + ex.getMessage());
        }
    }
}
