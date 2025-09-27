package com.mau.musicboxd.Review;


public class ReviewDTO {
    private Long user_id;
    private Long album_id;
    private String text;
    private Integer rating;
    
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public Long getAlbum_id() {
        return album_id;
    }
    public void setAlbum_id(Long album_id) {
        this.album_id = album_id;
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

    // getters and setters
    
}

