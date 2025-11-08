package com.mau.musicboxd.Review.dto;

import com.mau.musicboxd.Album.dto.AlbumDto;
import com.mau.musicboxd.Review.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long userId;
    private AlbumDto album;
    private String text;
    private Integer rating;
    
    public static ReviewRequestDto fromEntity(Review review){
        return ReviewRequestDto.builder()
                .userId(review.getUser().getId())
                .album(AlbumDto.fromEntity(review.getAlbum()))
                .text(review.getText())
                .rating(review.getRating())
                .build();
    }
    
}
