package view;

import model.User;
import util.ServiceRegistry;
import util.UITheme;
import util.DialogStyleUtil;
import util.MessageDialogUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;

public class MentorProfileModule extends JPanel {
    private User currentUser;
    private JPanel infoContainer;

    public MentorProfileModule(User user) {
        this.currentUser = user;
        setLayout(new GridBagLayout());
        setOpaque(false);
        
        initComponents();
    }

    private void initComponents() {
        removeAll();
        
        // Premium glassmorphic style card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BACKGROUND_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(UITheme.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(500, 500));
        card.setBorder(new EmptyBorder(35, 45, 35, 45));

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(UITheme.getColorForRole(currentUser.getRole()));
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                
                String fInit = (currentUser.getFirstName() != null && !currentUser.getFirstName().isEmpty()) ? currentUser.getFirstName().substring(0, 1) : "";
                String lInit = (currentUser.getLastName() != null && !currentUser.getLastName().isEmpty()) ? currentUser.getLastName().substring(0, 1) : "";
                String initials = (fInit + lInit).toUpperCase();
                
                int x = (getWidth() - fm.stringWidth(initials)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, x, y);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(80, 80));
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setOpaque(false);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("User Profile Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 25, 0));

        infoContainer = new JPanel(new GridLayout(0, 2, 10, 16));
        infoContainer.setOpaque(false);
        infoContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        addInfoRow("First Name", currentUser.getFirstName());
        addInfoRow("Last Name", currentUser.getLastName());
        addInfoRow("Email Address", currentUser.getEmail());
        addInfoRow("Phone Number", currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "Not set");
        addInfoRow("Account Role", currentUser.getRole().toString());

        JButton editBtn = new JButton("Edit Profile Details");
        editBtn.setPreferredSize(new Dimension(280, 42));
        editBtn.setMaximumSize(new Dimension(280, 42));
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #10B981; " +
            "foreground: #FFFFFF; " +
            "arc: 10; " +
            "hoverBackground: #059669; " +
            "focusedBackground: #10B981; " +
            "borderWidth: 0; " +
            "focusWidth: 0"
        );
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        editBtn.addActionListener(e -> showEditDialog());

        card.add(avatar);
        card.add(titleLabel);
        card.add(infoContainer);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(editBtn);

        add(card);
        revalidate();
        repaint();
    }

    private void addInfoRow(String label, String value) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(UITheme.TEXT_MUTED);
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        val.setForeground(UITheme.TEXT_PRIMARY);
        
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
