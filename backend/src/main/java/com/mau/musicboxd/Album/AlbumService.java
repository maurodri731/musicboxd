package com.mau.musicboxd.Album;

import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public class AlbumService {
    private final AlbumRespository albumRespository;

    public AlbumService(AlbumRespository albumRespository){
        this.albumRespository = albumRespository;
    }

    public void addAlbum(Album album){
        Optional<Album> albumOptional = albumRespository.findBySpotifyId(album.getSpotifyId());
        if(!albumOptional.isPresent()){
            albumRespository.save(album);
        }
    }
    
    public Album findAlbumById(Long albumId){
        Album album = albumRespository.findById(albumId)
            .orElseThrow(() -> new RuntimeException("User not found with id " + albumId));
        return album;
    }
}
