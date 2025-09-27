package com.mau.musicboxd.Album;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService){
        this.albumService = albumService;
    }

    @PostMapping("/album")
    public ResponseEntity<Album> registerNewAlbum(@RequestBody Album album){
        albumService.addAlbum(album);
        return ResponseEntity.status(201).body(album);
    }
}
