package com.mau.musicboxd.Review;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mau.musicboxd.Album.Album;
import com.mau.musicboxd.User.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    Optional<Review> findReviewByUserAndAlbum(Album album, User user);
}
