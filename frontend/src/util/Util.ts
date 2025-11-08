import axios from "axios";

export interface PopAlbum{
    albumId: string,
    albumName: string,
    artistId: string, 
    artistName: string,
    imageUrl: string,
    releaseDate: Date
}

export interface User{
  id: number,
  email: string,
  firstName: string,
  lastName: string,
  displayName: string,
  fullName: string,
  spotifyId: string,
  emailVerified: boolean,
  hasSpotifyConnected: boolean;
  createdAt: Date,
  lastLoginAt: Date
}

export interface AuthResponse {
  user: User,
  message: string
}

export interface AlbumResponse {
  albumId: string,
  albumName: string,
  artistName: string,
  imageUrl: string,
  releaseDate: Date
}

export interface ReviewResponse {
  userId: number,
  album: AlbumResponse,
  text: string,
  rating: number
}

export interface PageReviewResponse {
  content: ReviewResponse[],
  next: string | null,
  page: number,
  previous: string | null,
  total: number,
}
const api = axios.create({//call the api from here
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

export default api;