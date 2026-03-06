package com.houseofai.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ResendOtpRequest {

    @NotBlank
    private String tempId;

    public String getTempId() { return tempId; }
    public void setTempId(String tempId) { this.tempId = tempId; }
}
