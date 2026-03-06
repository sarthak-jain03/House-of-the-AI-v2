package com.houseofai.backend.service;

import com.houseofai.backend.dto.*;
import com.houseofai.backend.model.PendingUser;
import com.houseofai.backend.model.User;
import com.houseofai.backend.repository.PendingUserRepository;
import com.houseofai.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Random random = new Random();

    public AuthService(UserRepository userRepository,
                       PendingUserRepository pendingUserRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Converts a User entity to a safe UserResponse DTO.
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.from(user);
    }

    private String generateOtp() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    /**
     * Registers a pending user and sends OTP email.
     * @return the PendingUser id (tempId)
     * @throws IllegalArgumentException if email already registered
     */
    public String signup(SignupRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already registered.");
        }

        // Delete any previous pending signup for this email
        pendingUserRepository.deleteByEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String otp = generateOtp();
        String otpHash = passwordEncoder.encode(otp);
        Instant otpExpiry = Instant.now().plusSeconds(10 * 60);

        PendingUser pending = new PendingUser();
        pending.setName(request.getName());
        pending.setEmail(request.getEmail());
        pending.setPassword(hashedPassword);
        pending.setOtpHash(otpHash);
        pending.setOtpExpiry(otpExpiry);
        pending = pendingUserRepository.save(pending);

        emailService.sendOtpEmail(request.getEmail(), otp);

        return pending.getId();
    }

    /**
     * Verifies OTP and creates the actual User.
     * @return UserResponse DTO
     * @throws IllegalArgumentException if session expired, OTP expired, or OTP invalid
     */
    public UserResponse verifyOtp(OtpRequest request) {
        PendingUser pending = pendingUserRepository.findById(request.getTempId())
                .orElseThrow(() -> new IllegalArgumentException("Signup session expired."));

        if (pending.getOtpExpiry().isBefore(Instant.now())) {
            throw new IllegalArgumentException("OTP expired.");
        }

        if (!passwordEncoder.matches(request.getOtp(), pending.getOtpHash())) {
            throw new IllegalArgumentException("Invalid OTP.");
        }

        User user = new User();
        user.setName(pending.getName());
        user.setEmail(pending.getEmail());
        user.setPassword(pending.getPassword());
        user.setEmailVerified(true);
        user = userRepository.save(user);

        pendingUserRepository.deleteById(request.getTempId());

        return UserResponse.from(user);
    }

    /**
     * Regenerates OTP and resends email.
     * @throws IllegalArgumentException if signup session expired
     */
    public void resendOtp(String tempId) {
        PendingUser pending = pendingUserRepository.findById(tempId)
                .orElseThrow(() -> new IllegalArgumentException("Signup expired."));

        String otp = generateOtp();
        pending.setOtpHash(passwordEncoder.encode(otp));
        pending.setOtpExpiry(Instant.now().plusSeconds(10 * 60));
        pendingUserRepository.save(pending);

        emailService.sendOtpEmail(pending.getEmail(), otp);
    }

    /**
     * Validates credentials and returns the user response.
     * @return UserResponse DTO
     * @throws IllegalArgumentException if credentials invalid
     */
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return UserResponse.from(user);
    }

    /**
     * Finds or creates user from Google auth info.
     * @return UserResponse DTO
     */
    public UserResponse googleAuth(GoogleAuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setGoogleId(request.getGoogleId());
                    newUser.setName(request.getName());
                    newUser.setEmail(request.getEmail());
                    newUser.setEmailVerified(true);
                    return userRepository.save(newUser);
                });

        return UserResponse.from(user);
    }

    /**
     * Finds user by ID and returns the entity (for JWT token generation).
     */
    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }
}
