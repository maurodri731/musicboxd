package com.mau.musicboxd.SpotifySetup;

import java.io.IOException;
import java.net.URI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class AuthController {
    public static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
        .setClientId(ConfigKeys.API_KEY.getKey())
        .setClientSecret(ConfigKeys.API_SECRET.getKey())
        .setRedirectUri(redirectUri)
        .build();

    @GetMapping("login")
    @ResponseBody
    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .scope("user-read-private, user-read-email, user-top-read")
            .show_dialog(true)
            .build();
        

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    @GetMapping("/get-user-code")
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse resposnse) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        }
        catch(IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        resposnse.sendRedirect("http://localhost:5173/user-top-artists");
        return spotifyApi.getAccessToken();
    }

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
}
