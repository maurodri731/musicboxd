package com.mau.musicboxd.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")//I need this explained
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String text = String.format(
                "Hello,\n\n" +
                "You requested a password reset. Click the link below to reset your password:\n\n" +
                "%s\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Best regards,\n" +
                "MusicBoxd Team",
                resetUrl
            );
            
            message.setText(text);
            mailSender.send(message);
            
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendEmailVerification(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Email Verification");
            
            String verificationUrl = frontendUrl + "/verify-email?token=" + token;
            String text = String.format(
                "Hello,\n\n" +
                "Welcome to MusicBoxd! Please verify your email address by clicking the link below:\n\n" +
                "%s\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "Best regards,\n" +
                "MusicBoxd Team",
                verificationUrl
            );
            
            message.setText(text);
            mailSender.send(message);
            
            log.info("Email verification sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email verification to {}: {}", toEmail, e.getMessage());
        }
    }
}