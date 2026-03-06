package com.houseofai.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.houseofai.backend.model.User;

import java.time.Instant;

public class UserResponse {

    @JsonProperty("_id")
    private String id;
    private String name;
    private String email;
    private boolean emailVerified;
    private String googleId;
    private Instant createdAt;
    private Instant updatedAt;

    public UserResponse() {}

    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.id = user.getId();
        dto.name = user.getName();
        dto.email = user.getEmail();
        dto.emailVerified = user.isEmailVerified();
        dto.googleId = user.getGoogleId();
        dto.createdAt = user.getCreatedAt();
        dto.updatedAt = user.getUpdatedAt();
        return dto;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isEmailVerified() { return emailVerified; }
    public String getGoogleId() { return googleId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
