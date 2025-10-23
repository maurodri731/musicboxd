export interface PopAlbum{
    albumId: string,
    albumName: string,
    artistId: string, 
    artistName: string,
    imageUrl: string,
    releaseDate: Date
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