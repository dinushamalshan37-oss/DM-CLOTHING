package com.dmclothing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendOtpEmail(String email, String otp) {
        String subject = "DM Clothing OTP Verification";
        String body = "Your DM Clothing OTP code is: " + otp;

        sendEmail(email, subject, body);

        System.out.println("OTP Code for " + email + " = " + otp);
    }

    public void sendOrderStatusEmail(String email, String customerName, Long orderId, String status) {
        String subject = "DM Clothing Order Status Update";
        String body = "Hello " + customerName + ",\n\n"
                + "Your order #" + orderId + " status is now: " + status + ".\n\n"
                + "Thank you for shopping with DM Clothing.";

        sendEmail(email, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
                System.out.println("EMAIL NOT CONFIGURED");
                System.out.println("To: " + to);
                System.out.println("Subject: " + subject);
                System.out.println(body);
                return;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            System.out.println("Email sent to: " + to);

        } catch (Exception e) {
            System.out.println("Email send failed. Showing message in terminal.");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println(body);
        }
    }
}