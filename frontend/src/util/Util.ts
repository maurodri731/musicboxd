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

//This method can be refactored so that it works with all album requests?
export async function PopulateAlbums(apiUrl:string): Promise<PopAlbum[]> {
    const response = await fetch(apiUrl, {
        method: 'GET',
        headers: {
          'Access-Control-Allow-Origin': 'http://localhost:8080',
          'Content-Type': 'application/json',
        }
      });
    
    if(!response.ok){
        console.log(`Error in the landing page albums call ${response.status}`);
    }
    return response.json();
}

const api = axios.create({//call the api from here
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

export default api;