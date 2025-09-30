package com.mau.musicboxd.User;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "unique_email", columnNames = {"email"}),
    @UniqueConstraint(name = "unique_spotify_id", columnNames = {"spotify_id"})
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    private Long id;
    
    private String firstName;
    private String lastName;
    private String displayName; // For Spotify users
    private String email;
    private String password; // Nullable for Spotify-only users, and it will be hashed by the service
    private String spotifyId; // For Spotify OAuth users
    
    private boolean emailVerified = false;
    private boolean enabled = true;
    private boolean accountNonLocked = true;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime lastLoginAt;

    // Constructor for email/password registration
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Constructor for Spotify registration
    public User(String email, String displayName, String spotifyId) {
        this.email = email;
        this.displayName = displayName;
        this.spotifyId = spotifyId;
        this.emailVerified = true; // Spotify emails are pre-verified
    }

    // Computed property for full name
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return displayName != null ? displayName : email;
    }
}