package com.mau.musicboxd.auth;

import java.io.IOException;
import java.net.URI;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mau.musicboxd.User.User;

import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class SpotifyAuthController {
    public static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    private String code = "";
    private final SpotifyApi spotifyApi;
    private final AuthService authService;

    public SpotifyAuthController(SpotifyApi spotifyApi, AuthService authService){
        this.spotifyApi = spotifyApi;
        this.authService = authService;
    }

    @GetMapping("/spotify-login")//build the request so that the user gets redirected to the spotify login page... oAuth2!!!!
    @ResponseBody
    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .scope("user-read-private, user-read-email, user-top-read, playlist-read")//set the permissions the user will grant the server
            .show_dialog(true)
            .build();
        

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();//this return is exactly what sends you to the spotify login screen
    }

    @GetMapping("/get-user-code")//when the user has logged in they will be redirected here, where the access token and the refresh token are fetched from the api
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse resposnse) throws IOException {
        /*code = userCode;//the code used to fetch the access and refresh tokens is given with the redirect uri
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try{
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            //there is no need to save the tokens in variables separate from the Api object
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        }
        catch(IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        //when the access and refresh token are set, send them to the user detials page, this is the first thing the user sees after completing login with spotify*/
        User user = authService.registerSpotifyUser(userCode);
        resposnse.sendRedirect("http://localhost:5173/user-page");
        return spotifyApi.getAccessToken();//this last return allows the app to keep running with the spotify credentials
    }
}
