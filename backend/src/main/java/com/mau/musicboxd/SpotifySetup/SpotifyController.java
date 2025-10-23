package com.mau.musicboxd.SpotifySetup;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.mau.musicboxd.Album.Album;
import com.mau.musicboxd.SpotifySetup.dto.PopAlbumsDto;

import se.michaelthelin.spotify.model_objects.specification.Artist;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("user-top-artists")
    public Artist[] getUserTopArtists() {
        return spotifyService.getUserTopArtists();
    }

    @GetMapping("user-profile")
    public String getUserProfile() {
        return spotifyService.getUserProfile();
    }

    @GetMapping("album-search")
    public Album getAlbumFromApi(@RequestParam String albumId) {
        return spotifyService.getAlbumFromApi(albumId);
    }

    @GetMapping("most-popular")
    public List<PopAlbumsDto> getPopularAlbumsFromApi(){
        return spotifyService.getPopularAlbums();
    }

    @GetMapping("search-albums")
    public List<PopAlbumsDto> getAlbums(@RequestParam("query") String param) {
        return spotifyService.getAlbumSearch(param);
    }
    
}
