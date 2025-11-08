package com.mau.musicboxd.Album.dto;

import java.time.LocalDate;

import com.mau.musicboxd.Album.Album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {
    private Long albumId;
    private String spotifyId;
    private String albumName;
    private String artistName;
    private String imageUrl;
    private LocalDate releaseDate;

    public static AlbumDto fromEntity(Album album){
        return AlbumDto.builder()
                .albumId(album.getId())
                .spotifyId(album.getSpotifyId())
                .albumName(album.getTitle())
                .artistName(album.getArtist())
                .imageUrl(album.getCoverUrl())
                .releaseDate(album.getReleaseDate())
                .build();
    }

    public static java.util.List<AlbumDto> fromEntities(java.util.List<Album> albums){
        return albums.stream()
                    .map(AlbumDto::fromEntity)
                    .toList();
    }
}
