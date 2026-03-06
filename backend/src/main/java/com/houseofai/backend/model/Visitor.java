package com.houseofai.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "visitors")
public class Visitor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String visitorId;

    private Instant createdAt = Instant.now();

    public Visitor() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVisitorId() { return visitorId; }
    public void setVisitorId(String visitorId) { this.visitorId = visitorId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
