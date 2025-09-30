package com.mau.musicboxd.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import se.michaelthelin.spotify.SpotifyApi;

@Configuration
public class SpotifyConfig {

    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    @Value("${spotify.redirect.uri}")
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

