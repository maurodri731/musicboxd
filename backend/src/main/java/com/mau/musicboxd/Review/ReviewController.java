package com.mau.musicboxd.Review;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mau.musicboxd.Album.Album;
import com.mau.musicboxd.Album.AlbumService;
import com.mau.musicboxd.Review.dto.ReviewDTO;
import com.mau.musicboxd.User.User;
import com.mau.musicboxd.User.UserService;

@RestController
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final AlbumService albumService;

    public ReviewController(ReviewService reviewService, UserService userService, AlbumService albumService){
        this.reviewService = reviewService;
        this.userService = userService;
        this.albumService = albumService;
    }

    @Transactional
    @PostMapping("/review")
    public ResponseEntity<Review> addNewReview(@RequestBody ReviewDTO review){
        //the albumservice.addAlbum checks if the album exists in the database
        //so the album can be created and the function called, if it exists it just won't do anything.
        //the album has to be created first, however, you don't want the review to not find the album
        //this should probably be a transaction, but it would't be bad if the album got added but the review didn't go through?
        Album albumReviewed;
        if(!albumService.existsBySpotifyId(review.getAlbum().getAlbumId())){//only create a new album if it doesn't already exist (duhhh!)
            albumReviewed = new Album(review.getAlbum().getAlbumId(), review.getAlbum().getAlbumName() 
                , review.getAlbum().getArtistName(), LocalDate.parse(review.getAlbum().getReleaseDate()) , review.getAlbum().getImageUrl());
            albumService.addAlbum(albumReviewed);
        }
        else{//if the album already exists then just set that as the album that will be associated with the review
            albumReviewed = albumService.findBySpotifyId(review.getAlbum().getAlbumId());
        }
        Review newReview = new Review();
        User user = userService.findUserById(review.getUser_id());
        newReview.setAlbum(albumReviewed);
        newReview.setUser(user);
        newReview.setText(review.getText());
        newReview.setRating(review.getRating());
        reviewService.addReview(newReview);
        return ResponseEntity.status(201).body(newReview);
    }
}
