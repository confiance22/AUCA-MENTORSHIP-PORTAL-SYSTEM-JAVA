package controller;

import model.User;
import util.ServiceRegistry;
import view.DashboardView;
import view.LoginView;
import javax.swing.JOptionPane;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getLoginButton().addActionListener(e -> authenticateUser());
        view.getRegisterButton().addActionListener(e -> {
            view.dispose();
            new view.RegisterView().setVisible(true);
        });
    }

    private void authenticateUser() {
        String email = view.getEmailField().getText();
        String password = new String(view.getPasswordField().getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter both email and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = ServiceRegistry.userService.login(email, password);
            if (user != null) {
                if (!user.isIsActive()) {
                    JOptionPane.showMessageDialog(view, "Your account is not active. Please verify your OTP during registration.", "Account Inactive", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(view, "Login Successful! Welcome " + user.getFirstName());
                view.dispose();
                DashboardView dashboardView = new DashboardView(user);
                dashboardView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "An error occurred while communicating with the server.\n" + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
