package com.mau.musicboxd.User.dto;

import com.mau.musicboxd.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String fullName;
    private String spotifyId;
    private boolean emailVerified;
    private boolean hasSpotifyConnected;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    // Static factory method to create DTO from entity
    public static UserDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .spotifyId(user.getSpotifyId())
                .emailVerified(user.isEmailVerified())
                .hasSpotifyConnected(user.getSpotifyId() != null)
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    // Method to convert multiple entities
    public static java.util.List<UserDto> fromEntities(java.util.List<User> users) {
        return users.stream()
                .map(UserDto::fromEntity)
                .toList();
    }
}
