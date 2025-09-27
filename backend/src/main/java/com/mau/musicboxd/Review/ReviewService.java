package com.mau.musicboxd.Review;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mau.musicboxd.Album.AlbumRespository;
import com.mau.musicboxd.SpotifySetup.SpotifyService;
import com.mau.musicboxd.Album.Album;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AlbumRespository albumRespository;
    private final SpotifyService spotifyService;
    
    public ReviewService(ReviewRepository reviewRepository, AlbumRespository albumRespository
    , SpotifyService spotifyService){
        this.reviewRepository = reviewRepository;
        this.albumRespository = albumRespository;
        this.spotifyService = spotifyService;
    }

    public void addReview(Review newReview){
        Optional<Album> albumOptional = albumRespository.findBySpotifyId(newReview.getAlbum().getSpotifyId());
        if(!albumOptional.isPresent()){//if the album does not exist in the database, add it
            Album additional = spotifyService.getAlbumFromApi(newReview.getAlbum().getSpotifyId());
            albumRespository.save(additional);
        }
        reviewRepository.save(newReview);
    }
}
