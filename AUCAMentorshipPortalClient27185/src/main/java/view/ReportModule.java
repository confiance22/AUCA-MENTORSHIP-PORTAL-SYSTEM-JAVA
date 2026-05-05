package view;

import model.User;
import util.ServiceRegistry;
import javax.swing.*;
import java.awt.*;

public class ReportModule extends JPanel {
    private User currentUser;

    public ReportModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("System Reports & Analytics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        contentPanel.add(new JLabel("Available Reports:"));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton genPdfBtn = new JButton("Generate Mentorship Summary (PDF)");
        JButton genExcelBtn = new JButton("Export Session Logs (Excel)");
        
        contentPanel.add(genPdfBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(genExcelBtn);

        genPdfBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Report generation started... Check server console for progress.");
            // In a real app, call ServiceRegistry.reportService.generateSummaryReport();
        });

        add(contentPanel, BorderLayout.CENTER);
    }
}
