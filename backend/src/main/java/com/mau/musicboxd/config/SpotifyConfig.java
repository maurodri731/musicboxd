package com.mau.musicboxd.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mau.musicboxd.SpotifySetup.ConfigKeys;

import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyConfig {
    private String clientId = ConfigKeys.API_KEY.getKey();
    private String clientSecret = ConfigKeys.API_SECRET.getKey();
    private String redirectUri;

    @Bean
    public SpotifyApi spotifyApi(){
        return new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(URI.create(redirectUri))
            .build();
    }
}

