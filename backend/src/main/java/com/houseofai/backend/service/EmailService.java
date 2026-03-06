package com.houseofai.backend.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
    void sendFeedbackNotification(String name, String email, String message);
}
