package com.interviewprephub.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String RESEND_API_URL = "https://api.resend.com/emails";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000;

    private final HttpClient httpClient;

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${mail.from}")
    private String mailFrom;

    public EmailService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @PostConstruct
    public void init() {
        logger.info("========== EMAIL SERVICE CONFIGURATION ==========");
        logger.info("Provider      : Resend (HTTP API)");
        logger.info("API URL       : {}", RESEND_API_URL);
        logger.info("API Key       : {}", resendApiKey != null && resendApiKey.length() > 8
                ? resendApiKey.substring(0, 8) + "****" : "NOT SET");
        logger.info("MAIL_FROM     : {}", mailFrom);
        logger.info("FRONTEND_URL  : {}", frontendUrl);
        logger.info("==================================================");
    }

    public void sendVerificationEmail(String to, String token) {
        String verificationUrl = frontendUrl + "/verify-email?token=" + token;
        String subject = "Verify your email - InterviewPrepHub";
        String body = "Welcome to InterviewPrepHub!\n\n" +
                "Please verify your email by clicking the link below:\n\n" +
                verificationUrl + "\n\n" +
                "If you didn't create this account, please ignore this email.\n\n" +
                "Best regards,\n" +
                "InterviewPrepHub Team";

        sendWithRetry(to, subject, body, "verification");
    }

    public void sendWelcomeEmail(String to, String fullName) {
        String subject = "Welcome to InterviewPrepHub!";
        String body = "Hi " + fullName + ",\n\n" +
                "Welcome to InterviewPrepHub! Your email has been verified successfully.\n\n" +
                "Start preparing for your interviews now!\n\n" +
                "Best regards,\n" +
                "InterviewPrepHub Team";

        sendWithRetry(to, subject, body, "welcome");
    }

    private void sendWithRetry(String to, String subject, String textBody, String emailType) {
        String jsonPayload = buildJsonPayload(to, subject, textBody);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                logger.info("Attempt {}/{} - Sending {} email to {} via Resend API",
                        attempt, MAX_RETRIES, emailType, to);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(RESEND_API_URL))
                        .header("Authorization", "Bearer " + resendApiKey)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                        .timeout(Duration.ofSeconds(15))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    logger.info("SUCCESS - {} email sent to {} on attempt {} (status: {})",
                            emailType, to, attempt, response.statusCode());
                    return;
                } else {
                    logger.error("FAILED attempt {}/{} - {} email to {}: HTTP {} - {}",
                            attempt, MAX_RETRIES, emailType, to,
                            response.statusCode(), response.body());
                }

            } catch (Exception e) {
                logger.error("FAILED attempt {}/{} - {} email to {}: {} - {}",
                        attempt, MAX_RETRIES, emailType, to,
                        e.getClass().getSimpleName(), e.getMessage());

                if (e.getCause() != null) {
                    logger.error("Root cause: {} - {}",
                            e.getCause().getClass().getSimpleName(), e.getCause().getMessage());
                }
            }

            if (attempt < MAX_RETRIES) {
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                logger.error("ALL {} RETRIES EXHAUSTED for {} email to {}. Email NOT sent.",
                        MAX_RETRIES, emailType, to);
            }
        }
    }

    private String buildJsonPayload(String to, String subject, String textBody) {
        return "{" +
                "\"from\":\"InterviewPrepHub <" + escapeJson(mailFrom) + ">\"," +
                "\"to\":[\"" + escapeJson(to) + "\"]," +
                "\"subject\":\"" + escapeJson(subject) + "\"," +
                "\"text\":\"" + escapeJson(textBody) + "\"" +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
