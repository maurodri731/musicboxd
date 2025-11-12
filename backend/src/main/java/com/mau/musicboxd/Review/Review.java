package com.mau.musicboxd.Review;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mau.musicboxd.Album.Album;
import com.mau.musicboxd.User.User;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "\"Review\"",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "album_id"}))
public class Review {

    @Id
    @SequenceGenerator(
        name = "review_sequence",
        sequenceName = "review_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "review_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String text;
    private int rating;
    @CreatedDate
    private LocalDate created_at;

    public Review(){

    }
    
    public Review(Album album, User user, String text, int rating){
        this.album = album;
        this.user = user;
        this.text = text;
        this.rating = rating;
    }

    
    public Long getId() {return id;}
    public LocalDate getCreated_at() {return created_at;}
    public Long getAlbum_id() {return this.album.getId();}
    public void setAlbum_id(Long album_id) {this.album.setId(album_id);}
    public Long getUser_id() {return user.getId();}
    public void setUser_id(Long user_id) {this.user.setId(user_id);}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    public int getRating() {return rating;}
    public void setRating(int rating) {this.rating = rating;}

    public Album getAlbum(){return this.album;}
    public User getUser(){return this.user;}
    public void setAlbum(Album album){this.album = album;}
    public void setUser(User user){this.user = user;}
}