package com.houseofai.backend.controller;

import com.houseofai.backend.dto.ApiResponse;
import com.houseofai.backend.dto.FeedbackRequest;
import com.houseofai.backend.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/api/feedback")
    public ResponseEntity<ApiResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        try {
            feedbackService.submitFeedback(request.getName(), request.getEmail(), request.getMessage());

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Feedback sent successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Unable to send feedback.")
                            .build());
        }
    }
}
