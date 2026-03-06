package com.houseofai.backend.controller;

import com.houseofai.backend.dto.ApiResponse;
import com.houseofai.backend.dto.SaveChatRequest;
import com.houseofai.backend.model.ChatMessage;
import com.houseofai.backend.model.User;
import com.houseofai.backend.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveChat(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SaveChatRequest request) {
        try {
            if (user == null) {
                return ResponseEntity.status(401)
                        .body(ApiResponse.builder().error("Unauthorized — No valid token").build());
            }

            chatService.saveChat(user.getId(), request.getAiType(), request.getMessage(), request.getResponse());

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Chat saved successfully.")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().error("Server error while saving chat.").build());
        }
    }

    @GetMapping("/history/{aiType}")
    public ResponseEntity<ApiResponse> getChatHistory(
            @AuthenticationPrincipal User user,
            @PathVariable String aiType) {
        try {
            List<ChatMessage> chats = chatService.getChatHistory(user.getId(), aiType);

            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .history(chats)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().error("Failed to fetch chat history.").build());
        }
    }
}
