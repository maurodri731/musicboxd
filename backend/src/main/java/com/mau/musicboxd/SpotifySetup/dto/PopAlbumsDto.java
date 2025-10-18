package com.mau.musicboxd.SpotifySetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopAlbumsDto {
    private String albumId;
    private String albumName;
    private String artistName;
    private String artistId;
    private String imageUrl;
    private String releaseDate;
}
