package com.mau.musicboxd.Review;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mau.musicboxd.Album.AlbumRespository;
import com.mau.musicboxd.Review.dto.ReviewRequestDto;
import com.mau.musicboxd.SpotifySetup.SpotifyService;
import com.mau.musicboxd.util.PageResponse;
import com.mau.musicboxd.Album.Album;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AlbumRespository albumRepository;
    private final SpotifyService spotifyService;
    
    public ReviewService(ReviewRepository reviewRepository, AlbumRespository albumRepository
    , SpotifyService spotifyService){
        this.reviewRepository = reviewRepository;
        this.albumRepository = albumRepository;
        this.spotifyService = spotifyService;
    }

    public void addReview(Review newReview){
        Optional<Album> albumOptional = albumRepository.findBySpotifyId(newReview.getAlbum().getSpotifyId());
        if(!albumOptional.isPresent()){//if the album does not exist in the database, add it
            Album additional = spotifyService.getAlbumFromApi(newReview.getAlbum().getSpotifyId());
            albumRepository.save(additional);
        }
        reviewRepository.save(newReview);
    }

    public PageResponse<ReviewRequestDto> getUserReviews(Long userId, Pageable pageable){
        Page<Review> reviewPage = reviewRepository.getByUserId(userId, pageable);
        return new PageResponse<ReviewRequestDto>(reviewPage.map(ReviewRequestDto::fromEntity), "");
    }
}
