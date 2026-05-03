package controller;

import model.User;
import model.UserRole;
import util.ServiceRegistry;
import view.LoginView;
import view.RegisterView;

import javax.swing.*;
import java.time.LocalDateTime;

public class RegisterController {

    private RegisterView view;

    public RegisterController(RegisterView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getRegisterButton().addActionListener(e -> registerUser());
        view.getBackToLoginButton().addActionListener(e -> {
            view.dispose();
            new LoginView().setVisible(true);
        });
    }

    private void registerUser() {
        String firstName   = view.getFirstNameField().getText().trim();
        String lastName    = view.getLastNameField().getText().trim();
        String email       = view.getEmailField().getText().trim();
        String phone       = view.getPhoneField().getText().trim();
        String password    = new String(view.getPasswordField().getPassword());
        String confirmPwd  = new String(view.getConfirmPasswordField().getPassword());
        String selectedRole = (String) view.getRoleComboBox().getSelectedItem();

        // --- Validations ---
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(view, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(view, "Passwords do not match. Please re-enter.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            view.getPasswordField().setText("");
            view.getConfirmPasswordField().setText("");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(view, "Password must be at least 6 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check if email already exists
            boolean emailExists = ServiceRegistry.userService.existsByEmail(email);
            if (emailExists) {
                JOptionPane.showMessageDialog(view, "An account with this email already exists.\nPlease use a different email.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Build the User object
            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPassword(password); // Server's registerUserRecord() will hash it
            newUser.setPhoneNumber(phone);
            newUser.setRole(UserRole.valueOf(selectedRole));
            newUser.setIsActive(true);
            newUser.setCreatedAt(LocalDateTime.now());

            // Call the RMI service to register
            User registered = ServiceRegistry.userService.registerUserRecord(newUser);

            if (registered != null) {
                JOptionPane.showMessageDialog(view,
                    "Registration successful! Welcome, " + registered.getFirstName() + "!\nYou can now log in.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
                new LoginView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Server error during registration:\n" + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
