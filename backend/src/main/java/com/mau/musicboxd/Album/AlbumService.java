package com.mau.musicboxd.Album;

import java.util.Optional;

import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;


@Service
public class AlbumService {
    private final AlbumRespository albumRespository;
    private final SpotifyApi spotifyApi;

    public AlbumService(AlbumRespository albumRespository, SpotifyApi spotifyApi){
        this.albumRespository = albumRespository;
        this.spotifyApi = spotifyApi;
    }

    public Album getAlbumFromApi(String queryString){//worry about this when the client logic for searching albums is actually implemented
        return null;
    }

    public void addAlbum(Album album){
        Optional<Album> albumOptional = albumRespository.findBySpotifyId(album.getSpotifyId());
        if(!albumOptional.isPresent()){
            albumRespository.save(album);
        }
    }
    
    public Album findAlbumById(Long albumId){
        Album album = albumRespository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("Album not found with id " + albumId));
        return album;
    }

    public boolean existsBySpotifyId(String spotifyId){
        return albumRespository.existsBySpotifyId(spotifyId);
    }

    public Album findBySpotifyId(String spotifyId){
        Album album = albumRespository.findBySpotifyId(spotifyId)
            .orElseThrow(() -> new RuntimeException("Album not found with spotifyId" + spotifyId));
        return album;
    }
}
