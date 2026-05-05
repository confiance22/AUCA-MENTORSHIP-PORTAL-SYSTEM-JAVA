package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MessageDialogUtil {

    private static final Color PRIMARY_COLOR = new Color(30, 58, 138);
    private static final Color SUCCESS_COLOR = new Color(5, 150, 105);
    private static final Color ERROR_COLOR = new Color(220, 38, 38);
    private static final Color WARNING_COLOR = new Color(245, 158, 11);

    public static void showSuccess(Component parent, String message) {
        showCustomDialog(parent, "Success", message, "✅", SUCCESS_COLOR);
    }

    public static void showError(Component parent, String message) {
        showCustomDialog(parent, "Error", message, "❌", ERROR_COLOR);
    }

    public static void showWarning(Component parent, String message) {
        showCustomDialog(parent, "Warning", message, "⚠️", WARNING_COLOR);
    }

    public static void showMessage(Component parent, String message) {
        showCustomDialog(parent, "Notification", message, "ℹ️", PRIMARY_COLOR);
    }

    public static String showInput(Component parent, String message, String title) {
        JPanel panel = createStyledPanel();
        panel.add(createFieldLabel(message));
        JTextField field = createStyledTextField("");
        panel.add(field);

        int option = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            return field.getText();
        }
        return null;
    }

    private static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(55, 65, 81));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        return label;
    }

    private static JTextField createStyledTextField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setPreferredSize(new Dimension(300, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    public static int showConfirm(Component parent, String message, String title) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel iconLabel = new JLabel("❓");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel msgLabel = new JLabel("<html><div style='width: 250px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(msgLabel, BorderLayout.CENTER);

        return JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    private static void showCustomDialog(Component parent, String title, String message, String emoji, Color themeColor) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel msgLabel = new JLabel("<html><div style='width: 280px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msgLabel.setForeground(new Color(31, 41, 55));
        panel.add(msgLabel, BorderLayout.CENTER);

        // Customize the button via UIManager temporarily for this call if possible, 
        // but simplest is just passing the panel to showMessageDialog.
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
}
