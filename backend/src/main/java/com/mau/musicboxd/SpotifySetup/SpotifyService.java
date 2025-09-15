package com.mau.musicboxd.SpotifySetup;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class SpotifyService {
    protected SpotifyApi spotifyApi = AuthController.spotifyApi;

    @GetMapping(value = "user-top-artists")
    public Artist[] getUserTopArtists() {
        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
            .time_range("medium_term")
            .limit(10)
            .offset(5)
            .build();
        
        try{
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        }
        catch (Exception e) {
            System.out.println("Something went wrong\n" + e.getMessage());
        }
        return new Artist[0];
    }

    @GetMapping(value = "user-profile")
    public String getUserPlaylists(){
        final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> userDetails = new HashMap<>();
        try{
            final User user = getCurrentUsersProfileRequest.execute();
            userDetails.put("displayName", user.getDisplayName());
            userDetails.put("email", user.getEmail());
            return objectMapper.writeValueAsString(userDetails);
        }
        catch(Exception e){
            System.out.println("Something went wrong\n" + e.getMessage());
        }
        return null;
    }
}