package view;

import model.User;
import util.ServiceRegistry;
import util.ButtonStyleUtil;
import util.MessageDialogUtil;
import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.File;

public class ReportModule extends JPanel {
    private User currentUser;

    public ReportModule(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("System Reports & Analytics", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 1, 10, 15));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        JButton usersPdfBtn = new JButton("Export All Users (PDF)");
        ButtonStyleUtil.applyPrimaryStyle(usersPdfBtn);
        
        JButton programsExcelBtn = new JButton("Export All Programs (Excel)");
        ButtonStyleUtil.applySuccessStyle(programsExcelBtn);
        
        JButton sessionsPdfBtn = new JButton("Export Session Logs (PDF)");
        ButtonStyleUtil.applyPrimaryStyle(sessionsPdfBtn);
        
        JButton feedbackPdfBtn = new JButton("Export Session Feedback (PDF)");
        ButtonStyleUtil.applyPrimaryStyle(feedbackPdfBtn);
        
        contentPanel.add(usersPdfBtn);
        contentPanel.add(programsExcelBtn);
        contentPanel.add(sessionsPdfBtn);
        contentPanel.add(feedbackPdfBtn);

        usersPdfBtn.addActionListener(e -> downloadReport("users_report.pdf", "PDF"));
        programsExcelBtn.addActionListener(e -> downloadReport("programs_report.xlsx", "Excel"));
        sessionsPdfBtn.addActionListener(e -> downloadReport("sessions_report.pdf", "PDF"));
        feedbackPdfBtn.addActionListener(e -> downloadReport("feedback_report.pdf", "PDF"));

        add(contentPanel, BorderLayout.CENTER);
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
