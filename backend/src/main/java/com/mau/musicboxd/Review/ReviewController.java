package com.mau.musicboxd.Review;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mau.musicboxd.Album.Album;
import com.mau.musicboxd.Album.AlbumService;
import com.mau.musicboxd.User.User;
import com.mau.musicboxd.User.UserService;

@RestController
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

    @PostMapping("/review")
    public ResponseEntity<Review> addNewReview(@RequestBody ReviewDTO reviewDto){
        Review review = new Review();
        User user = userService.findUserById(reviewDto.getUser_id());
        Album album = albumService.findAlbumById(reviewDto.getAlbum_id());
        review.setAlbum(album);
        review.setUser(user);
        review.setText(reviewDto.getText());
        review.setRating(reviewDto.getRating());
        reviewService.addReview(review);
        return ResponseEntity.status(201).body(review);
    }
}
