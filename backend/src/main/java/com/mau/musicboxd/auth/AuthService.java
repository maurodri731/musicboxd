package com.mau.musicboxd.auth;

import com.mau.musicboxd.auth.dto.RegisterRequest;
import com.mau.musicboxd.auth.exception.InvalidTokenException;
import com.mau.musicboxd.auth.exception.UserAlreadyExistsException;
import com.mau.musicboxd.auth.repository.PasswordResetTokenRepository;
import com.mau.musicboxd.auth.repository.EmailVerificationTokenRepository;
import com.mau.musicboxd.auth.entity.PasswordResetToken;
import com.mau.musicboxd.auth.entity.EmailVerificationToken;
import com.mau.musicboxd.User.User;
import com.mau.musicboxd.User.UserRepository;

import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final SpotifyApi spotifyApi;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailVerificationTokenRepository emailVerificationTokenRepository,
            SpotifyApi spotifyApi) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.spotifyApi = spotifyApi;
    }

    /**
     * Register a new user with email/password
     */
    public User registerUser(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - user already exists: {}", request.getEmail());
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(false);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setDisplayName(request.getDisplayName());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        // Send email verification
        sendEmailVerification(savedUser);

        return savedUser;
    }

    /**
     * Authenticate user with email and password
     */
    public User authenticateUser(String email, String password) {
        log.info("Attempting to authenticate user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Authentication failed - user not found: {}", email);
                    return new BadCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Authentication failed - invalid password for: {}", email);
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!user.isAccountNonLocked()) {
            log.warn("Authentication failed - account locked: {}", email);
            throw new BadCredentialsException("Account is locked");
        }

        if (!user.isEnabled()) {
            log.warn("Authentication failed - account disabled: {}", email);
            throw new BadCredentialsException("Account is disabled");
        }

        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User authenticated successfully: {}", email);
        return user;
    }

    /**
     * Register user from Spotify OAuth
     */
    public User registerSpotifyUser(String code) {
        //--------first use the code to fetch the tokens
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
        String refreshToken = null;
        String accessToken = null;
        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            refreshToken = authorizationCodeCredentials.getRefreshToken();
            accessToken = authorizationCodeCredentials.getAccessToken();
            spotifyApi.setAccessToken(accessToken);
            spotifyApi.setRefreshToken(refreshToken);
            //there is no need to save the tokens in variables separate from the Api object
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        }
        catch(IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            System.out.println("Error setting access and refresh tokens for the user" + e.getMessage());
        }

        //-----------Now use the tokens to fetch the users information
        final GetCurrentUsersProfileRequest request = spotifyApi.getCurrentUsersProfile().build();
        se.michaelthelin.spotify.model_objects.specification.User requestUser = null;//need this to map the api answer
        try{
            requestUser = request.execute();
        }
        catch(Exception e){
            System.out.println("Error fetching user information" + e.getMessage());
        }

        //---------And now save the users information
        User savedUser = null;
        if(requestUser != null){
            log.info("Attempting to register Spotify user: {}", requestUser.getEmail());

            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(requestUser.getEmail());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                // Update Spotify ID if not set
                if (user.getSpotifyId() == null) {
                    user.setSpotifyId(requestUser.getId());
                    user = userRepository.save(user);
                }
                log.info("Existing user logged in via Spotify: {}", requestUser.getEmail());
                return user;
            }

            // Create new user from Spotify
            User user = new User();
            user.setEmail(requestUser.getEmail());
            user.setDisplayName(requestUser.getDisplayName());
            user.setSpotifyId(requestUser.getId());
            user.setEmailVerified(true); // Spotify emails are pre-verified
            user.setAccountNonLocked(true);
            user.setEnabled(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setLastLoginAt(LocalDateTime.now());
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            savedUser = userRepository.save(user);
            log.info("Spotify user registered successfully: {}", savedUser.getEmail());
        }
        return savedUser;
    }

    /**
     * Initiate password reset process
     */
    public void initiatePasswordReset(String email) {
        log.info("Password reset requested for: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.warn("Password reset requested for non-existent user: {}", email);
            return; // Don't reveal if user exists
        }

        User user = userOpt.get();

        // Delete any existing reset tokens for this user
        passwordResetTokenRepository.deleteByUserId(user.getId());

        // Create new reset token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // 1 hour expiry
        passwordResetTokenRepository.save(resetToken);

        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
        log.info("Password reset email sent to: {}", email);
    }

    /**
     * Reset password using token
     */
    public void resetPassword(String token, String newPassword) {
        log.info("Password reset attempt with token: {}", token);

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Password reset failed - token expired: {}", token);
            throw new InvalidTokenException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the used token
        passwordResetTokenRepository.delete(resetToken);

        log.info("Password reset successfully for user: {}", user.getEmail());
    }

    /**
     * Send email verification
     */
    public void sendEmailVerification(User user) {
        // Delete any existing verification tokens
        emailVerificationTokenRepository.deleteByUserId(user.getId());

        // Create new verification token
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // 24 hour expiry
        emailVerificationTokenRepository.save(verificationToken);

        // Send verification email
        emailService.sendEmailVerification(user.getEmail(), verificationToken.getToken());
        log.info("Email verification sent to: {}", user.getEmail());
    }

    /**
     * Verify email using token
     */
    public void verifyEmail(String token) {
        log.info("Email verification attempt with token: {}", token);

        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Email verification failed - token expired: {}", token);
            throw new InvalidTokenException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        // Delete the used token
        emailVerificationTokenRepository.delete(verificationToken);

        log.info("Email verified successfully for user: {}", user.getEmail());
    }

    /**
     * Check if user exists by email
     */
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Resend email verification
     */
    public void resendEmailVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (user.isEmailVerified()) {
            log.info("Email already verified for user: {}", email);
            return;
        }

        sendEmailVerification(user);
    }
}
