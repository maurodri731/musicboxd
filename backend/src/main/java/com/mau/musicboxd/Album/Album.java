package com.mau.musicboxd.Album;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"Album\"")
public class Album {
    
    @Id
    @SequenceGenerator(
        name = "album_sequence",
        sequenceName = "album_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "album_sequence"
    )
    private Long id;
    //Design consideration: I could save just the spotifyId and the rest of the informatio
    //woild be attached to it, but that would require another query to the Spotify API
    //which will increase latency and waste tokens
    private String spotifyId;
    private String title;
    private String artist;
    private LocalDate releaseDate;
    private String coverUrl;

    public Album(){
        
    }
    public Album(String spotifyId, String title, String artist, LocalDate releaseDate, String coverUrl) {
        this.spotifyId = spotifyId;
        this.title = title;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.coverUrl = coverUrl;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getSpotifyId() {return spotifyId;}
    public void setSpotifyId(String spotifyId) {this.spotifyId = spotifyId;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getArtist() {return artist;}
    public void setArtist(String artist) {this.artist = artist;}
    public LocalDate getReleaseDate() {return releaseDate;}
    public void setReleaseDate(LocalDate releaseDate) {this.releaseDate = releaseDate;}
    public String getCoverUrl() {return coverUrl;}
    public void setCoverUrl(String coverUrl) {this.coverUrl = coverUrl;}

}
