package view;

import model.User;
import util.ServiceRegistry;
import util.ButtonStyleUtil;
import util.DialogStyleUtil;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MentorProfileModule extends JPanel {
    private User currentUser;
    private JPanel infoContainer;

    public MentorProfileModule(User user) {
        this.currentUser = user;
        setLayout(new GridBagLayout());
        setBackground(new Color(243, 244, 246)); 
        
        initComponents();
    }

    private void initComponents() {
        removeAll();
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(500, 550));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(40, 50, 40, 50)
        ));

        JLabel avatar = new JLabel("👤", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("User Profile", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(17, 24, 39));
        titleLabel.setBorder(new EmptyBorder(10, 0, 30, 0));

        infoContainer = new JPanel(new GridLayout(0, 2, 10, 20));
        infoContainer.setBackground(Color.WHITE);
        infoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        addInfoRow("First Name", currentUser.getFirstName());
        addInfoRow("Last Name", currentUser.getLastName());
        addInfoRow("Email", currentUser.getEmail());
        addInfoRow("Phone", currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "Not set");
        addInfoRow("Role", currentUser.getRole().toString());

        JButton editBtn = new JButton("Edit Profile Details");
        ButtonStyleUtil.applySuccessStyle(editBtn);
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        editBtn.addActionListener(e -> showEditDialog());

        card.add(avatar);
        card.add(titleLabel);
        card.add(infoContainer);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        card.add(editBtn);

        add(card);
        revalidate();
        repaint();
    }

    private void addInfoRow(String label, String value) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(107, 114, 128));
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        val.setForeground(new Color(17, 24, 39));
        
        infoContainer.add(lbl);
        infoContainer.add(val);
    }

    private void showEditDialog() {
        JPanel panel = DialogStyleUtil.createStyledPanel();
        
        JTextField fName = DialogStyleUtil.createStyledTextField(currentUser.getFirstName());
        JTextField lName = DialogStyleUtil.createStyledTextField(currentUser.getLastName());
        JTextField phone = DialogStyleUtil.createStyledTextField(currentUser.getPhoneNumber());

        panel.add(DialogStyleUtil.createFieldLabel("First Name:"));
        panel.add(fName);
        panel.add(DialogStyleUtil.createFieldLabel("Last Name:"));
        panel.add(lName);
        panel.add(DialogStyleUtil.createFieldLabel("Phone Number:"));
        panel.add(phone);

        int option = JOptionPane.showConfirmDialog(this, panel, "Update Profile Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                currentUser.setFirstName(fName.getText().trim());
                currentUser.setLastName(lName.getText().trim());
                currentUser.setPhoneNumber(phone.getText().trim());

                ServiceRegistry.userService.updateUserRecord(currentUser);
                MessageDialogUtil.showSuccess(this, "Profile updated successfully!");
                initComponents(); 
            } catch (Exception ex) {
                MessageDialogUtil.showError(this, "Error updating profile: " + ex.getMessage());
            }
        }
    }
}
