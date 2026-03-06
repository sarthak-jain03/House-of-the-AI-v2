package com.houseofai.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class OtpRequest {

    @NotBlank
    private String tempId;

    @NotBlank
    private String otp;

    public String getTempId() { return tempId; }
    public void setTempId(String tempId) { this.tempId = tempId; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
