package com.interviewprephub.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;

    @PostConstruct
    public void init() {
        logger.info("========== EMAIL SERVICE CONFIGURATION ==========");
        logger.info("MAIL_HOST     : {}", mailHost);
        logger.info("MAIL_PORT     : {}", mailPort);
        logger.info("MAIL_USERNAME : {}",
                mailUsername != null ? mailUsername.substring(0, Math.min(4, mailUsername.length())) + "****" : "NULL");
        logger.info("FRONTEND_URL  : {}", frontendUrl);
        logger.info("JavaMailSender class: {}", mailSender.getClass().getName());

        if (mailSender instanceof JavaMailSenderImpl impl) {
            var props = impl.getJavaMailProperties();
            logger.info("smtp.auth         : {}", props.getProperty("mail.smtp.auth"));
            logger.info("smtp.starttls     : {}", props.getProperty("mail.smtp.starttls.enable"));
            logger.info("smtp.ssl.trust    : {}", props.getProperty("mail.smtp.ssl.trust"));
            logger.info("smtp.ssl.protocols: {}", props.getProperty("mail.smtp.ssl.protocols"));
        }
        logger.info("==================================================");
    }

    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = frontendUrl + "/verify-email?token=" + token;
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

        sendWithRetry(message, to, "verification");
    }

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

        sendWithRetry(message, to, "welcome");
    }

    private void sendWithRetry(SimpleMailMessage message, String to, String emailType) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                logger.info("Attempt {}/{} - Sending {} email to {} via {}:{}",
                        attempt, MAX_RETRIES, emailType, to, mailHost, mailPort);
                mailSender.send(message);
                logger.info("SUCCESS - {} email sent to {} on attempt {}", emailType, to, attempt);
                return;
            } catch (Exception e) {
                logger.error("FAILED attempt {}/{} - {} email to {}: {} - {}",
                        attempt, MAX_RETRIES, emailType, to,
                        e.getClass().getSimpleName(), e.getMessage());

                if (e.getCause() != null) {
                    logger.error("Root cause: {} - {}", e.getCause().getClass().getSimpleName(),
                            e.getCause().getMessage());
                }

                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    logger.error("ALL {} RETRIES EXHAUSTED for {} email to {}. Email NOT sent.", MAX_RETRIES, emailType,
                            to);
                }
            }
        }
    }
}
