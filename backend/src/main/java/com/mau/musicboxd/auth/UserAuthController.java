package com.mau.musicboxd.auth;

import com.mau.musicboxd.auth.dto.*;
import com.mau.musicboxd.auth.exception.InvalidTokenException;
import com.mau.musicboxd.auth.exception.UserAlreadyExistsException;
import com.mau.musicboxd.User.User;
import com.mau.musicboxd.User.UserService;
import com.mau.musicboxd.User.dto.UserDto;//need to make a userDTO
import com.mau.musicboxd.config.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
public class UserAuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserAuthController(AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());
        
        try {
            User user = authService.registerUser(request);
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            AuthResponse response = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(UserDto.fromEntity(user))
                    .message("Registration successful")
                    .build();
                    
            log.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed - user already exists: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthResponse.builder()
                            .message("User with this email already exists")
                            .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        try {
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            AuthResponse response = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(UserDto.fromEntity(user))
                    .message("Login successful")
                    .build();
                    
            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed - invalid credentials for: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid email or password")
                            .build());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            if (!jwtUtil.isTokenValid(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.builder()
                                .message("Invalid or expired refresh token")
                                .build());
            }
            
            String email = jwtUtil.getEmailFromToken(refreshToken);
            User user = userService.findByEmail(email);
            
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            
            AuthResponse response = AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .user(UserDto.fromEntity(user))
                    .message("Token refreshed successfully")
                    .build();
                    
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Token refresh failed")
                            .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtUtil.blacklistToken(token);
        }
        
        Map<String, String> response = Map.of("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());
        
        authService.initiatePasswordReset(request.getEmail());
        Map<String, String> response = Map.of(
            "message", "If an account with this email exists, a password reset link has been sent"
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            Map<String, String> response = Map.of("message", "Password reset successfully");
            return ResponseEntity.ok(response);
            
        } catch (InvalidTokenException e) {
            Map<String, String> response = Map.of("message", "Invalid or expired reset token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        try {
            authService.verifyEmail(token);
            Map<String, String> response = Map.of("message", "Email verified successfully");
            return ResponseEntity.ok(response);
            
        } catch (InvalidTokenException e) {
            Map<String, String> response = Map.of("message", "Invalid or expired verification token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}