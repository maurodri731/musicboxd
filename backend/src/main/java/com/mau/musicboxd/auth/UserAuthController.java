package com.mau.musicboxd.auth;

import com.mau.musicboxd.auth.dto.*;
import com.mau.musicboxd.auth.exception.InvalidTokenException;
import com.mau.musicboxd.auth.exception.UserAlreadyExistsException;
import com.mau.musicboxd.User.User;
import com.mau.musicboxd.User.UserService;
import com.mau.musicboxd.User.dto.UserDto;
import com.mau.musicboxd.config.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, 
                                                  HttpServletResponse response) {
        log.info("Registration attempt for email: {}", request.getEmail());
        
        try {
            User user = authService.registerUser(request);
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            // Set tokens as HTTP-only cookies
            setAuthCookies(response, accessToken, refreshToken);
            
            AuthResponse authResponse = AuthResponse.builder()
                    .user(UserDto.fromEntity(user))
                    .message("Registration successful")
                    .build();
                    
            log.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
            
        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed - user already exists: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthResponse.builder()
                            .message("User with this email already exists")
                            .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        try {
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            
            // Set tokens as HTTP-only cookies
            setAuthCookies(response, accessToken, refreshToken);
            
            AuthResponse authResponse = AuthResponse.builder()
                    .user(UserDto.fromEntity(user))
                    .message("Login successful")
                    .build();
                    
            log.info("User logged in successfully: {}", user.getEmail());
            return ResponseEntity.ok(authResponse);
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed - invalid credentials for: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid email or password")
                            .build());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request,
                                                     HttpServletResponse response) {
        try {
            // Extract refresh token from cookie
            String refreshToken = getTokenFromCookie(request, "refreshToken");
            
            if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.builder()
                                .message("Invalid or expired refresh token")
                                .build());
            }
            
            String email = jwtUtil.getEmailFromToken(refreshToken);
            User user = userService.findByEmail(email);
            
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            
            // Set new tokens as cookies
            setAuthCookies(response, newAccessToken, newRefreshToken);
            
            AuthResponse authResponse = AuthResponse.builder()
                    .user(UserDto.fromEntity(user))
                    .message("Token refreshed successfully")
                    .build();
                    
            return ResponseEntity.ok(authResponse);
            
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Token refresh failed")
                            .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request,
                                                       HttpServletResponse response) {
        // Extract access token from cookie for blacklisting
        String accessToken = getTokenFromCookie(request, "accessToken");
        if (accessToken != null) {
            jwtUtil.blacklistToken(accessToken);
        }
        
        // Clear auth cookies
        clearAuthCookies(response);
        
        Map<String, String> responseBody = Map.of("message", "Logged out successfully");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request, HttpServletResponse response){
        try{
            String accessToken = getTokenFromCookie(request,"accessToken");

            if (accessToken == null || !jwtUtil.isTokenValid(accessToken)) {
                
                ResponseEntity<AuthResponse> refreshResponse = refreshToken(request, response);
                if(refreshResponse.getStatusCode() != HttpStatus.OK) {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(AuthResponse.builder()
                                    .message("There is no authenticated user")
                                    .user(null)
                                    .build());
                }

                accessToken = getTokenFromCookie(request, "accessToken");

                if(accessToken == null){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Refreshing access token failed"));
                }
            }

            String email = jwtUtil.getEmailFromToken(accessToken);
            User user = userService.findByEmail(email);
            AuthResponse authResponse = AuthResponse.builder()
                                        .user(UserDto.fromEntity(user))
                                        .message("Cookies verified")
                                        .build();

            return ResponseEntity.ok(authResponse);

        } catch(Exception e) {
            log.error("Error when fetching user details from cookies", e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(AuthResponse.builder()
                            .message("Error when fetching user details from cookies")
                            .build());
        }
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

    // Helper methods for cookie management
    private void setAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(15 * 60) // 15 minutes
                .sameSite("Lax")
                .build();
        
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax")
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}