package com.houseofai.backend.controller;

import com.houseofai.backend.dto.ApiResponse;
import com.houseofai.backend.service.VisitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/api/visitors")
    public ResponseEntity<ApiResponse> trackVisitor(
            @RequestParam(required = false) String visitorId) {
        try {
            long count = visitorService.trackVisitor(visitorId);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .count(count)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder()
                            .success(false)
                            .count(0L)
                            .build());
        }
    }
}
