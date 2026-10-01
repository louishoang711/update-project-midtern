package com.hcmute.bookstore.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hcmute.bookstore.service.MailService_24162056;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailServiceImpl_24162056 implements MailService_24162056 {
    private final Properties config = new Properties();

    public MailServiceImpl_24162056() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) config.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể đọc cấu hình email", exception);
        }
    }

    @Override
    public void sendOtp(String recipient, String otp) {
        if (!Boolean.parseBoolean(config.getProperty("mail.enabled", "false"))) {
            System.out.printf("[BookStore OTP] email=%s | otp=%s%n", recipient, otp);
            return;
        }

        Properties mail = new Properties();
        mail.put("mail.smtp.auth", "true");
        mail.put("mail.smtp.starttls.enable", "true");
        mail.put("mail.smtp.host", config.getProperty("mail.host"));
        mail.put("mail.smtp.port", config.getProperty("mail.port", "587"));
        String username = config.getProperty("mail.username");
        String password = config.getProperty("mail.password");
        Session session = Session.getInstance(mail, new jakarta.mail.Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getProperty("mail.from", username)));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Mã xác thực BookStore", "UTF-8");
            message.setText("Mã OTP của bạn là: " + otp + ". Mã có hiệu lực trong 5 phút.", "UTF-8");
            Transport.send(message);
        } catch (Exception exception) {
            throw new IllegalStateException("Không gửi được mã OTP. Hãy kiểm tra cấu hình mail.", exception);
        }
    }
}
