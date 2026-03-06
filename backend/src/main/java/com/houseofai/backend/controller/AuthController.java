package com.houseofai.backend.controller;

import com.houseofai.backend.dto.*;
import com.houseofai.backend.model.User;
import com.houseofai.backend.service.AuthService;
import com.houseofai.backend.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<ApiResponse> respondWithToken(UserResponse userDto, ApiResponse body) {
        String token = jwtUtil.generateTokenFromId(userDto.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtUtil.createAuthCookie(token).toString())
                .body(body);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        try {
            String tempId = authService.signup(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("OTP sent. Please verify to complete signup.")
                    .tempId(tempId)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().message("Signup failed.").build());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@Valid @RequestBody OtpRequest request) {
        try {
            UserResponse userDto = authService.verifyOtp(request);
            return respondWithToken(userDto, ApiResponse.builder()
                    .message("Signup complete!")
                    .user(userDto)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().message("OTP verification failed.").build());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        try {
            authService.resendOtp(request.getTempId());
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("OTP resent successfully.")
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().message("Could not resend OTP.").build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserResponse userDto = authService.login(request);
            return respondWithToken(userDto, ApiResponse.builder()
                    .message("Login successful")
                    .user(userDto)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().message("Login failed.").build());
        }
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse> googleAuth(@Valid @RequestBody GoogleAuthRequest request) {
        try {
            UserResponse userDto = authService.googleAuth(request);
            return respondWithToken(userDto, ApiResponse.builder()
                    .message("Google login successful")
                    .user(userDto)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.builder().message("Google auth failed.").build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtUtil.createLogoutCookie().toString())
                .body(ApiResponse.builder().message("Logged out successfully").build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.builder()
                .user(authService.toUserResponse(user))
                .build());
    }
}
