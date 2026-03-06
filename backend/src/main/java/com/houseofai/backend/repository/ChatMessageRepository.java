package com.houseofai.backend.repository;

import com.houseofai.backend.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByUserIdAndAiTypeOrderByTimestampAsc(String userId, String aiType);
}
