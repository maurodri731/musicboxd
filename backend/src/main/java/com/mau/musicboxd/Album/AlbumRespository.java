package com.mau.musicboxd.Album;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlbumRespository extends JpaRepository<Album, Long> {
    
    Optional<Album> findBySpotifyId(String spotifyId);
}
