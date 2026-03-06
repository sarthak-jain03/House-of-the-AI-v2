package com.houseofai.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "pendingusers")
public class PendingUser {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String otpHash;
    private Instant otpExpiry;

    public PendingUser() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtpHash() { return otpHash; }
    public void setOtpHash(String otpHash) { this.otpHash = otpHash; }

    public Instant getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(Instant otpExpiry) { this.otpExpiry = otpExpiry; }
}
