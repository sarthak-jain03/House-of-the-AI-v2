package com.houseofai.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "chatmessages")
@CompoundIndex(name = "user_ai_idx", def = "{'userId': 1, 'aiType': 1}")
public class ChatMessage {

    @Id
    private String id;
    private String userId;
    private String aiType;
    private String message;
    private String response;
    private Instant timestamp;

    public ChatMessage() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAiType() { return aiType; }
    public void setAiType(String aiType) { this.aiType = aiType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
