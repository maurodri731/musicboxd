package com.mau.musicboxd.Review.dto;

import com.mau.musicboxd.SpotifySetup.dto.PopAlbumsDto;

public class ReviewDto {
    private Long user_id;
    private PopAlbumsDto album;
    private String text;
    private Integer rating;
    
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public PopAlbumsDto getAlbum() {
        return album;
    }
    public void setAlbum(PopAlbumsDto album) {
        this.album = album;
    }
    // getters and setters
    
}

