package com.interviewprephub.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendVerificationEmail(String to, String token) {
    	logger.info("Frontend URL from config: {}", frontendUrl);

    	if (!StringUtils.hasText(frontendUrl)) {
    	    frontendUrl = "https://interview-prep-hub-frontend-gamma.vercel.app";
    	}

    	logger.info("Frontend URL used for email: {}", frontendUrl);
        String verificationUrl = frontendUrl + "/verify-email?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
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
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String to, String fullName) {
        SimpleMailMessage message = new SimpleMailMessage();
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
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
