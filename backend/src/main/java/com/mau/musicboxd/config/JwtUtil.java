package com.mau.musicboxd.config;

import com.mau.musicboxd.User.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenValidityInMinutes;
    private final long refreshTokenValidityInDays;
    private final String issuer;
    
    // In-memory blacklist for logged out tokens
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public JwtUtil(
            @Value("${jwt.secret:myDefaultSecretKeyThatIsAtLeast256BitsLongForHS256Algorithm}") String secret,
            @Value("${jwt.access-token-validity-minutes:15}") long accessTokenValidityInMinutes,
            @Value("${jwt.refresh-token-validity-days:7}") long refreshTokenValidityInDays,
            @Value("${jwt.issuer:musicboxd}") String issuer) {
        
        // Generate secret key from string (ensure it's at least 256 bits for HS256)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityInMinutes = accessTokenValidityInMinutes;
        this.refreshTokenValidityInDays = refreshTokenValidityInDays;
        this.issuer = issuer;
    }

    /**
     * Generate access token for user
     */
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        claims.put("emailVerified", user.isEmailVerified());
        claims.put("spotifyConnected", user.getSpotifyId() != null);
        
        return createToken(claims, user.getEmail(), accessTokenValidityInMinutes, ChronoUnit.MINUTES);
    }

    /**
     * Generate refresh token for user
     */
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("tokenType", "refresh");
        
        return createToken(claims, user.getEmail(), refreshTokenValidityInDays, ChronoUnit.DAYS);
    }

    /**
     * Create JWT token with given parameters
     */
    private String createToken(Map<String, Object> claims, String subject, long validity, ChronoUnit unit) {
        Instant now = Instant.now();
        Instant expiration = now.plus(validity, unit);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extract email from token
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extract user ID from token
     */
    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extract expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extract specific claim from token
     */
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token", e);
        }
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * Validate token against user details
     */
    public boolean validateToken(String token, User user) {
        try {
            if (isTokenBlacklisted(token)) {
                log.warn("Token is blacklisted");
                return false;
            }

            String emailFromToken = getEmailFromToken(token);
            Long userIdFromToken = getUserIdFromToken(token);
            
            return emailFromToken.equals(user.getEmail()) 
                && userIdFromToken.equals(user.getId())
                && !isTokenExpired(token);
                
        } catch (JwtException e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is valid (not expired and properly formed)
     */
    public boolean isTokenValid(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            
            getAllClaimsFromToken(token); // This will throw exception if invalid
            return !isTokenExpired(token);
            
        } catch (JwtException e) {
            log.error("Token is invalid: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get token type (access or refresh)
     */
    public String getTokenType(String token) {
        return getClaimFromToken(token, claims -> claims.get("tokenType", String.class));
    }

    /**
     * Check if token is a refresh token
     */
    public boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenType(token));
    }

    /**
     * Add token to blacklist (for logout)
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
        log.info("Token blacklisted successfully");
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * Extract token from Authorization header
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Get remaining validity time in minutes
     */
    public long getRemainingValidityInMinutes(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            long remainingMs = expiration.getTime() - System.currentTimeMillis();
            return Math.max(0, remainingMs / (1000 * 60));
        } catch (JwtException e) {
            return 0;
        }
    }

    /**
     * Clean up expired tokens from blacklist (call this periodically)
     */
    public void cleanupExpiredBlacklistedTokens() {
        blacklistedTokens.removeIf(token -> {
            try {
                return isTokenExpired(token);
            } catch (JwtException e) {
                return true; // Remove invalid tokens
            }
        });
        log.info("Cleaned up expired blacklisted tokens");
    }
}