package com.mau.musicboxd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "myDefaultSecretKeyThatIsAtLeast256BitsLongForHS256Algorithm";
    private long accessTokenValidityMinutes = 15;
    private long refreshTokenValidityDays = 7;
    private String issuer = "musicboxd";

    // Getters and setters
    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getAccessTokenValidityMinutes() { return accessTokenValidityMinutes; }
    public void setAccessTokenValidityMinutes(long accessTokenValidityMinutes) {
        this.accessTokenValidityMinutes = accessTokenValidityMinutes;
    }

    public long getRefreshTokenValidityDays() { return refreshTokenValidityDays; }
    public void setRefreshTokenValidityDays(long refreshTokenValidityDays) {
        this.refreshTokenValidityDays = refreshTokenValidityDays;
    }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}
