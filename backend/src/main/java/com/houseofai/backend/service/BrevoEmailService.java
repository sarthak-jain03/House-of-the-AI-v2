package com.houseofai.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailService.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.brevo.api-key}")
    private String brevoApiKey;

    @Value("${app.email.sender}")
    private String senderEmail;

    @Value("${app.email.feedback-to}")
    private String feedbackToEmail;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        String htmlContent = """
                <h2>Email Verification Code</h2>
                <p>Enter the following OTP to complete your signup:</p>
                <h1 style="color:#4CAF50; font-size:32px;">%s</h1>
                <p>This OTP expires in <strong>10 minutes</strong>.</p>
                """.formatted(otp);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "House of the AI", "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", "Your OTP Verification Code",
                "htmlContent", htmlContent
        );

        sendEmail(body);
        log.info("OTP sent successfully to {}", toEmail);
    }

    @Override
    public void sendFeedbackNotification(String name, String email, String message) {
        String htmlContent = """
                <h2>New Feedback Received</h2>
                <p><strong>Name:</strong> %s</p>
                <p><strong>Email:</strong> %s</p>
                <p><strong>Message:</strong></p>
                <p>%s</p>
                """.formatted(name, email, message);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "House of the AI", "email", feedbackToEmail),
                "to", List.of(Map.of("email", feedbackToEmail)),
                "replyTo", Map.of("email", email),
                "subject", "New Feedback from " + name,
                "htmlContent", htmlContent
        );

        sendEmail(body);
        log.info("Feedback notification sent for {}", email);
    }

    private void sendEmail(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            throw new RuntimeException("Could not send email. Please try again.", e);
        }
    }
}
