package com.houseofai.backend.service;

import com.houseofai.backend.model.Feedback;
import com.houseofai.backend.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;

    public FeedbackService(FeedbackRepository feedbackRepository, EmailService emailService) {
        this.feedbackRepository = feedbackRepository;
        this.emailService = emailService;
    }

    /**
     * Saves feedback to DB and sends email notification via Brevo.
     */
    public void submitFeedback(String name, String email, String message) {
        Feedback feedback = new Feedback();
        feedback.setName(name);
        feedback.setEmail(email);
        feedback.setMessage(message);
        feedbackRepository.save(feedback);

        emailService.sendFeedbackNotification(name, email, message);
    }
}
