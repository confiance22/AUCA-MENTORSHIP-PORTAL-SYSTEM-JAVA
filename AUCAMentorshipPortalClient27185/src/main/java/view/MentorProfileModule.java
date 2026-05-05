package view;

import model.User;
import javax.swing.*;
import java.awt.*;

public class MentorProfileModule extends JPanel {
    private User currentUser;

    public MentorProfileModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("User Profile Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        contentPanel.add(new JLabel("First Name:"));
        contentPanel.add(new JLabel(currentUser.getFirstName()));
        
        contentPanel.add(new JLabel("Last Name:"));
        contentPanel.add(new JLabel(currentUser.getLastName()));
        
        contentPanel.add(new JLabel("Email:"));
        contentPanel.add(new JLabel(currentUser.getEmail()));
        
        contentPanel.add(new JLabel("Phone:"));
        contentPanel.add(new JLabel(currentUser.getPhoneNumber()));
        
        contentPanel.add(new JLabel("Role:"));
        contentPanel.add(new JLabel(currentUser.getRole().toString()));

        add(contentPanel, BorderLayout.CENTER);
        
        JButton editBtn = new JButton("Edit Profile");
        add(editBtn, BorderLayout.SOUTH);
    }
}
