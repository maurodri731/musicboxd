import { Music, Search } from "lucide-react";
import React, { useEffect, useState } from 'react';
import { PopulateAlbums, PopAlbum } from "../util/Util";
import NavbarComp from "../components/UtilComps/NavbarComp";
import AlbumCard from "../components/AlbumSearchComps/AlbumCard";
import Modal from "../components/UtilComps/Modal";
import ReviewForm from "../components/AlbumSearchComps/ReviewForm";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function SearchAlbums(){
    const [search, setSearch] = useState('');
    const [albums, setAlbums] = useState<PopAlbum[]>([]);
    const [searchLoading, setIsLoading] = useState(false);
    const [modalState, setModalState] = useState<{ isOpen: boolean; selectedAlbum: PopAlbum | null}>({isOpen:false, selectedAlbum:null});
    const navigate = useNavigate();
    const { user, loading } = useAuth();

    useEffect( () => {//check if the user is signed-in, if they aren't then redirect them to the sign up page
      if(loading) return;
      if(user === null){
        navigate("/auth?mode=sign-up")
      }
    }, [])

    const handleSubmit = async (e: React.FormEvent) => {//submitting logic
      e.preventDefault();

      try{
        const albumsList = await PopulateAlbums(`http://localhost:8080/api/search-albums?query=${search}`)//search logic for the album search
        setAlbums(albumsList);
      } catch (err) {
        console.log("Unable to load the albums for this artist", err);
      } finally {
        setIsLoading(true);
      }
  };

  const handleClick = (album: PopAlbum) => {//handles the storing of the album details for the modal to use
    console.log("Album clicked");
    setModalState({ isOpen: true, selectedAlbum:album});
  };

  //sample data to test the modal
  const sampleAlbum:PopAlbum = {albumId:"1234", albumName:"Random Access Memories", artistId:"1234", artistName:"Daft Punk", imageUrl:"https://i.scdn.co/image/ab67616d0000b2736fcdcbbd9cae9001ca5b20d5", releaseDate: new Date("2025-12-25")}

    return (
        <>
          <NavbarComp/>

          <div className="max-w-6xl mx-auto px-4 md:px-6 lg:px-8 pt-8 mb-12">
            <h2 className="p-1 text-white text-lg font-semibold">Log away!</h2>
            <div className="p-2 bg-gradient-to-r from-purple-500 via-pink-500 to-blue-500 rounded-lg">
              <div className="relative bg-gray-900 rounded-lg">
                <Music className="absolute left-4 top-1/2 transform -translate-y-1/2 text-purple-400 w-5 h-5" />
                <form className="flex" onSubmit={handleSubmit}>
                  <input
                    type="text"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    placeholder="Search albums by title or artist name..."
                    className="w-full pl-12 pr-4 py-3 bg-transparent text-white placeholder-gray-400 focus:outline-none rounded-lg"
                  />
                  <button type="submit" className="px-6 py-3 bg-purple-600 hover:bg-purple-700 text-white font-semibold rounded-lg transition flex items-center gap-2">
                    <Search className="w-5 h-5" />
                    Search
                  </button>
                </form>
              </div>
            </div>
          </div>


          <div className="max-w-6xl mx-auto px-4 md:px-6 lg:px-8">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 pb-12">
              {
                albums.map((album) => (
                  <AlbumCard key={album.albumId} onClick={() => handleClick(album)} title={album.albumName} artist={album.artistName} cover={album.imageUrl} /> 
              ))}
              <AlbumCard onClick={() => handleClick(sampleAlbum)} title="Random Access Memories" artist="Pearl Jam" cover="https://i.scdn.co/image/ab67616d0000b2736fcdcbbd9cae9001ca5b20d5"/>  
              <AlbumCard onClick={() => handleClick(sampleAlbum)} title="Random Access Memories" artist="Pearl Jam" cover="https://i.scdn.co/image/ab67616d0000b2736fcdcbbd9cae9001ca5b20d5"/>  
              <AlbumCard onClick={() => handleClick(sampleAlbum)} title="Random Access Memories" artist="Pearl Jam" cover="https://i.scdn.co/image/ab67616d0000b2736fcdcbbd9cae9001ca5b20d5"/>  
              <AlbumCard onClick={() => handleClick(sampleAlbum)} title="Random Access Memories" artist="Pearl Jam" cover="https://i.scdn.co/image/ab67616d0000b2736fcdcbbd9cae9001ca5b20d5"/>  
            </div>
          </div>

          <Modal isOpen={modalState.isOpen} onClose={() => setModalState({isOpen:false, selectedAlbum:null})}>
            <ReviewForm album={modalState.selectedAlbum}/>
          </Modal>
        </>
    )
}