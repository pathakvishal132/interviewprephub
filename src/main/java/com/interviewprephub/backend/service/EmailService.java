package com.interviewprephub.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Async
    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = "https://interview-prep-hub-frontend-gamma.vercel.app" + "/verify-email?token="
                + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(to);
        message.setSubject("Verify your email - InterviewPrepHub");
        message.setText(
                "Welcome to InterviewPrepHub!\n\n" +
                        "Please verify your email by clicking the link below:\n\n" +
                        verificationUrl + "\n\n" +
                        "If you didn't create this account, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "InterviewPrepHub Team");

        try {
            mailSender.send(message);
            logger.info("Verification email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Async
    public void sendWelcomeEmail(String to, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(to);
        message.setSubject("Welcome to InterviewPrepHub!");
        message.setText(
                "Hi " + fullName + ",\n\n" +
                        "Welcome to InterviewPrepHub! Your email has been verified successfully.\n\n" +
                        "Start preparing for your interviews now!\n\n" +
                        "Best regards,\n" +
                        "InterviewPrepHub Team");

        try {
            mailSender.send(message);
            logger.info("Welcome email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", to, e.getMessage(), e);
        }
    }
}
