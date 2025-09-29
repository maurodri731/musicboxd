package com.mau.musicboxd.SpotifySetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mau.musicboxd.Album.Album;

import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@Service
public class SpotifyService {

    private final SpotifyApi spotifyApi;

    public SpotifyService(SpotifyApi spotifyApi){
        this.spotifyApi = spotifyApi;
    }

    public Artist[] getUserTopArtists() {
        final GetUsersTopArtistsRequest request = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

        try {
            Paging<Artist> artistPaging = request.execute();
            return artistPaging.getItems();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch top artists", e);
        }
    }

    public String getUserProfile() {
        final GetCurrentUsersProfileRequest request = spotifyApi.getCurrentUsersProfile().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> userDetails = new HashMap<>();
        try {
            User user = request.execute();
            userDetails.put("displayName", user.getDisplayName());
            userDetails.put("email", user.getEmail());
            return objectMapper.writeValueAsString(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user profile", e);
        }
    }

    public Album getAlbumFromApi(String albumId) {
        try {
            GetAlbumRequest request = spotifyApi.getAlbum(albumId).build();
            se.michaelthelin.spotify.model_objects.specification.Album spotifyAlbum = request.execute();

            Album album = new Album();
            album.setSpotifyId(spotifyAlbum.getId());
            album.setTitle(spotifyAlbum.getName());
            album.setArtist(spotifyAlbum.getArtists()[0].getName());

            // Parse release date safely
            String releaseDateStr = spotifyAlbum.getReleaseDate();
            if (releaseDateStr != null) {
                LocalDate releaseDate = null;
                if (releaseDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    releaseDate = LocalDate.parse(releaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } else if (releaseDateStr.matches("\\d{4}-\\d{2}")) {
                    releaseDate = LocalDate.parse(releaseDateStr + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
                } else if (releaseDateStr.matches("\\d{4}")) {
                    releaseDate = LocalDate.parse(releaseDateStr + "-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
                }
                album.setReleaseDate(releaseDate);
            }

            return album;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch album from Spotify", e);
        }
    }
}
