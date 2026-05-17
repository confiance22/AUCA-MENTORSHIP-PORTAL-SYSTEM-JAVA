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
        showCustomDialog(parent, "Success", message, "✓", SUCCESS_COLOR); // Minimalist checkmark
    }

    public static void showError(Component parent, String message) {
        showCustomDialog(parent, "Error", message, "✕", ERROR_COLOR); // Minimalist cross
    }

    public static void showWarning(Component parent, String message) {
        showCustomDialog(parent, "Warning", message, "⚠", WARNING_COLOR); // Minimalist warning
    }

    public static void showMessage(Component parent, String message) {
        showCustomDialog(parent, "Notification", message, "ℹ", PRIMARY_COLOR); // Minimalist info
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
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        return panel;
    }

    private static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setBorder(new EmptyBorder(5, 0, 5, 0));
        return label;
    }

    private static JTextField createStyledTextField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setPreferredSize(new Dimension(220, 34)); // Ultra-compact search/input field
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return field;
    }

    public static int showConfirm(Component parent, String message, String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 8));
        panel.setBorder(new EmptyBorder(6, 10, 6, 10));

        JLabel iconLabel = new JLabel("?");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        iconLabel.setForeground(PRIMARY_COLOR);
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel msgLabel = new JLabel("<html><div style='width: 210px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(msgLabel, BorderLayout.CENTER);

        return JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    private static void showCustomDialog(Component parent, String title, String message, String symbol, Color themeColor) {
        JPanel panel = new JPanel(new BorderLayout(12, 10));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel iconLabel = new JLabel(symbol);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 26)); // Balanced, sleek indicator
        iconLabel.setForeground(themeColor);
        panel.add(iconLabel, BorderLayout.WEST);

        JLabel msgLabel = new JLabel("<html><div style='width: 210px;'>" + message + "</div></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Clean, standard body font size
        panel.add(msgLabel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
}
