package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogStyleUtil {

    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        return label;
    }

    public static JTextField createStyledTextField(String initialValue) {
        JTextField field = new JTextField(initialValue);
        field.setPreferredSize(new Dimension(300, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }
}
