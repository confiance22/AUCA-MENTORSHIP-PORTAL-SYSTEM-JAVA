package controller;

import model.User;
import util.ServiceRegistry;
import util.MessageDialogUtil;
import view.DashboardView;
import view.LoginView;
import view.RegisterView;
import view.OTPVerificationView;
import model.UserRole;
import java.awt.event.ActionEvent;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        this.view.getLoginButton().addActionListener(this::handleLogin);
        this.view.getRegisterButton().addActionListener(e -> {
            view.dispose();
            new RegisterView().setVisible(true);
        });
    }

    private void handleLogin(ActionEvent e) {
        String email = view.getEmailField().getText().trim();
        String password = new String(view.getPasswordField().getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            MessageDialogUtil.showError(view, "Please enter both email and password.");
            return;
        }

        try {
            User user = ServiceRegistry.userService.login(email, password);
            if (user != null) {
                if (!user.isIsActive()) {
                    MessageDialogUtil.showWarning(view, "Your account is not active. Please verify your OTP during registration.");
                    return;
                }
                
                if (user.getRole() != UserRole.ADMIN) {
                    ServiceRegistry.notificationService.sendOtpNotification(user.getId(), user.getEmail());
                    MessageDialogUtil.showSuccess(view, "OTP has been sent to your email for verification.");
                    view.dispose();
                    new OTPVerificationView(user, true).setVisible(true);
                } else {
                    MessageDialogUtil.showSuccess(view, "Login Successful! Welcome " + user.getFirstName());
                    view.dispose();
                    new DashboardView(user).setVisible(true);
                }
            } else {
                MessageDialogUtil.showError(view, "Invalid email or password.");
            }
        } catch (Exception ex) {
            MessageDialogUtil.showError(view, "An error occurred while communicating with the server.\n" + ex.getMessage());
        }
    }
}
