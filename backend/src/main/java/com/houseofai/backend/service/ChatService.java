package com.houseofai.backend.service;

import com.houseofai.backend.model.ChatMessage;
import com.houseofai.backend.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    private static final Set<String> VALID_AI_TYPES = Set.of("poet", "coder", "story", "datasage");

    private final ChatMessageRepository chatMessageRepository;

    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * Saves a chat message for the given user.
     * @throws IllegalArgumentException if aiType is invalid
     */
    public void saveChat(String userId, String aiType, String message, String response) {
        if (!VALID_AI_TYPES.contains(aiType)) {
            throw new IllegalArgumentException("Invalid AI type: " + aiType);
        }

        ChatMessage chat = new ChatMessage();
        chat.setUserId(userId);
        chat.setAiType(aiType);
        chat.setMessage(message);
        chat.setResponse(response);
        chat.setTimestamp(Instant.now());
        chatMessageRepository.save(chat);
    }

    /**
     * Retrieves chat history for a user and AI type, sorted by timestamp ascending.
     * @throws IllegalArgumentException if aiType is invalid
     */
    public List<ChatMessage> getChatHistory(String userId, String aiType) {
        if (!VALID_AI_TYPES.contains(aiType)) {
            throw new IllegalArgumentException("Invalid AI type: " + aiType);
        }

        return chatMessageRepository.findByUserIdAndAiTypeOrderByTimestampAsc(userId, aiType);
    }
}
