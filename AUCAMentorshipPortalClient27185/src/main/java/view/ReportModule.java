package view;

import model.User;
import util.ServiceRegistry;
import util.UITheme;
import util.MessageDialogUtil;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.File;
import com.formdev.flatlaf.FlatClientProperties;

public class ReportModule extends JPanel {
    private User currentUser;

    public ReportModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("System Reports & Analytics", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        // Card Container Wrapper for modern SaaS visual alignment
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        JPanel reportCard = new JPanel() {
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
        reportCard.setOpaque(false);
        reportCard.setLayout(new GridLayout(0, 1, 15, 20));
        reportCard.setPreferredSize(new Dimension(500, 360));
        reportCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel descLabel = new JLabel("Select a system analytic metric to export:", SwingConstants.LEFT);
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        descLabel.setForeground(UITheme.TEXT_MUTED);
        reportCard.add(descLabel);

        JButton usersPdfBtn = new JButton("Export All Users (PDF)");
        usersPdfBtn.setPreferredSize(new Dimension(0, 42));
        usersPdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usersPdfBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        usersPdfBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #4F46E5; " +
            "foreground: #FFFFFF; " +
            "arc: 10; " +
            "hoverBackground: #4338CA; " +
            "focusWidth: 0"
        );
        
        JButton programsExcelBtn = new JButton("Export All Programs (Excel)");
        programsExcelBtn.setPreferredSize(new Dimension(0, 42));
        programsExcelBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        programsExcelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        programsExcelBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #10B981; " +
            "foreground: #FFFFFF; " +
            "arc: 10; " +
            "hoverBackground: #059669; " +
            "focusWidth: 0"
        );
        
        JButton sessionsPdfBtn = new JButton("Export Session Logs (PDF)");
        sessionsPdfBtn.setPreferredSize(new Dimension(0, 42));
        sessionsPdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sessionsPdfBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sessionsPdfBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #16171D; " +
            "foreground: #F3F4F6; " +
            "arc: 10; " +
            "hoverBackground: #262930; " +
            "borderWidth: 1; " +
            "borderColor: #262930; " +
            "focusWidth: 0"
        );
        
        JButton feedbackPdfBtn = new JButton("Export Session Feedback (PDF)");
        feedbackPdfBtn.setPreferredSize(new Dimension(0, 42));
        feedbackPdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feedbackPdfBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        feedbackPdfBtn.putClientProperty(FlatClientProperties.STYLE, 
            "background: #16171D; " +
            "foreground: #F3F4F6; " +
            "arc: 10; " +
            "hoverBackground: #262930; " +
            "borderWidth: 1; " +
            "borderColor: #262930; " +
            "focusWidth: 0"
        );
        
        reportCard.add(usersPdfBtn);
        reportCard.add(programsExcelBtn);
        reportCard.add(sessionsPdfBtn);
        reportCard.add(feedbackPdfBtn);

        usersPdfBtn.addActionListener(e -> downloadReport("users_report.pdf", "PDF"));
        programsExcelBtn.addActionListener(e -> downloadReport("programs_report.xlsx", "Excel"));
        sessionsPdfBtn.addActionListener(e -> downloadReport("sessions_report.pdf", "PDF"));
        feedbackPdfBtn.addActionListener(e -> downloadReport("feedback_report.pdf", "PDF"));

        cardWrapper.add(reportCard);
        add(cardWrapper, BorderLayout.CENTER);
    }

    private void downloadReport(String defaultName, String type) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(defaultName));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] data = null;
                if (defaultName.contains("users")) data = ServiceRegistry.reportService.exportUsersToPdf();
                else if (defaultName.contains("programs")) data = ServiceRegistry.reportService.exportProgramsToExcel();
                else if (defaultName.contains("sessions")) data = ServiceRegistry.reportService.exportSessionsToPdf();
                else if (defaultName.contains("feedback")) data = ServiceRegistry.reportService.exportFeedbackToPdf();

                if (data != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(data);
                    }
                    MessageDialogUtil.showSuccess(this, "Report saved successfully!");
                }
            } catch (Exception ex) {
                MessageDialogUtil.showError(this, "Error generating report: " + ex.getMessage());
            }
        }
    }
}
