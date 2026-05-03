package service.impl;

import dao.MentorshipProgramDao;
import dao.UserDao;
import dao.impl.MentorshipProgramDaoImpl;
import dao.impl.UserDaoImpl;
import model.MentorshipProgram;
import model.User;
import service.ReportService;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ReportServiceImpl extends UnicastRemoteObject implements ReportService {

    private final UserDao userDao;
    private final MentorshipProgramDao mentorshipProgramDao;

    public ReportServiceImpl() throws RemoteException {
        super();
        this.userDao = new UserDaoImpl();
        this.mentorshipProgramDao = new MentorshipProgramDaoImpl();
    }

    @Override
    public byte[] exportUsersToPdf() throws RemoteException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Mentorship Portal - Users Report").setBold().setFontSize(16));
            document.add(new Paragraph(" ")); // empty line

            List<User> users = userDao.findAll();

            float[] pointColumnWidths = {50F, 150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);
            table.addCell("ID");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Role");

            for (User user : users) {
                table.addCell(String.valueOf(user.getId()));
                table.addCell(user.getFirstName());
                table.addCell(user.getLastName());
                table.addCell(user.getRole().name());
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating PDF: " + e.getMessage());
        }
    }

    @Override
    public byte[] exportProgramsToExcel() throws RemoteException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Programs");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Status");
            headerRow.createCell(3).setCellValue("Enrolled");

            List<MentorshipProgram> programs = mentorshipProgramDao.findAll();
            int rowNum = 1;
            for (MentorshipProgram program : programs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(program.getId());
                row.createCell(1).setCellValue(program.getTitle());
                row.createCell(2).setCellValue(program.getStatus().name());
                row.createCell(3).setCellValue(mentorshipProgramDao.countEnrolled(program.getId()));
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating Excel: " + e.getMessage());
        }
    }
}
