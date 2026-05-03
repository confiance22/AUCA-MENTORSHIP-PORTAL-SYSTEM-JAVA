package util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Properties;

public class OTPUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    // Placeholders - replace with real credentials in production
    private static final String SMTP_USERNAME = "confianceufitamahoro22@gmail.com"; 
    private static final String SMTP_PASSWORD = "yeet fatu drdi rxfy";

    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP() {
        int otp = 100000 + random.nextInt(900000); // Generate 6-digit OTP
        return String.valueOf(otp);
    }

    public static void sendOTPEmail(String toEmail, String otpCode) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("AUCA Mentorship Portal - Your OTP Code");
            message.setText("Hello,\n\nYour OTP code is: " + otpCode + "\n\nThis code will expire in 5 minutes.\n\nThank you!");

            Transport.send(message);
            System.out.println("OTP Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email to " + toEmail + ": " + e.getMessage());
            // We just log it so it doesn't crash the server if email fails
        }
    }
}
