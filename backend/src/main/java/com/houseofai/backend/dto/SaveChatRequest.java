package com.houseofai.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SaveChatRequest {

    @NotBlank
    private String aiType;

    @NotBlank
    private String message;

    @NotBlank
    private String response;

    public String getAiType() { return aiType; }
    public void setAiType(String aiType) { this.aiType = aiType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}
