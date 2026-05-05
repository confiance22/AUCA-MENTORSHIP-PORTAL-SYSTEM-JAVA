package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonStyleUtil {

    private static final Color PRIMARY_BLUE = new Color(30, 58, 138);
    private static final Color SUCCESS_GREEN = new Color(5, 150, 105);
    private static final Color DANGER_RED = new Color(220, 38, 38);
    private static final Color HOVER_OVERLAY = new Color(255, 255, 255, 30);

    public static void applyPrimaryStyle(JButton btn) {
        applyBaseStyle(btn, PRIMARY_BLUE);
    }

    public static void applySuccessStyle(JButton btn) {
        applyBaseStyle(btn, SUCCESS_GREEN);
    }

    public static void applyDangerStyle(JButton btn) {
        applyBaseStyle(btn, DANGER_RED);
    }

    private static void applyBaseStyle(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(8, 20, 8, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
    }
}
