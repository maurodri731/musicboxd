import { Heart, Users, TrendingUp, Disc3 } from 'lucide-react';
import { useState, useEffect } from 'react';
import NavbarComp from '../components/UtilComps/NavbarComp';
import { PopulateAlbums, PopAlbum } from '../util/Util';
import { useAuth } from '../context/AuthContext';

export default function Landing() {
  const { user } = useAuth();
  console.log("Landing page rendering", user);
  const [albums, setAlbums] = useState<PopAlbum[]>([]);

  useEffect(() => {
    PopulateAlbums('http://localhost:8080/api/most-popular')
      .then(albumList => setAlbums(albumList))
      .catch(err => {
        console.error('Failed to load albums:', err);
      });
  }, []);

  return (
    <>
    <div className="min-h-screen relative">
      <NavbarComp />
      
      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 md:px-6 lg:px-8 py-12">
        <div className="lg:w-2/3">
          {/* Badge */}
          <div className="inline-block px-4 py-2 bg-purple-600 bg-opacity-20 border border-purple-600 rounded-full text-sm mb-6 text-white">
            ✨ Join music enthusiasts
          </div>
          
          {/* Hero Title */}
          <h1 className="text-4xl md:text-7xl font-bold mb-8 leading-tight bg-gradient-to-r from-white to-gray-400 bg-clip-text text-transparent">
            Collect Your Musical Journey
          </h1>
          
          {/* Description */}
          <p className="text-xl text-gray-400 leading-relaxed mb-8">
            Rate albums, create lists, and share your music taste with a community that truly gets it. Your personal music diary starts here.
          </p>
          
          {/* CTA Buttons */}
          <div className="flex gap-3 mb-12 flex-wrap">
            <button className="inline-flex items-center gap-2 bg-white text-black px-8 py-4 rounded-lg font-bold hover:bg-gray-200 transition-colors">
              <Heart size={20} />
              Start Collection
            </button>
            <button className="bg-gray-800 text-white px-8 py-4 rounded-lg font-bold hover:bg-gray-700 transition-colors">
              Browse Albums
            </button>
          </div>

          {/* Album Grid */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-12">
            {albums.map((album) => (
              <div key={album.albumId}>
                <div className="aspect-square bg-gradient-to-br from-gray-800 to-gray-900 rounded-lg shadow-2xl border border-gray-800 hover:scale-105 transition-transform cursor-pointer overflow-hidden">
                  <img 
                    src={album.imageUrl} 
                    alt={`${album.albumName} Album cover`}
                    className="w-full h-full object-cover"
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="max-w-7xl mx-auto px-4 md:px-6 lg:px-8 pb-12">
        <div className="grid md:grid-cols-3 gap-6">
          {/* Feature 1 */}
          <div className="bg-gray-900 bg-opacity-50 backdrop-blur-lg p-6 rounded-xl border border-gray-800">
            <Disc3 className="w-10 h-10 text-purple-400 mb-4" />
            <h3 className="text-xl font-bold mb-2 text-white">
              Unlimited Reviews
            </h3>
            <p className="text-gray-400">
              Write as many reviews as you want. No limits on your expression.
            </p>
          </div>

          {/* Feature 2 */}
          <div className="bg-gray-900 bg-opacity-50 backdrop-blur-lg p-6 rounded-xl border border-gray-800">
            <Users className="w-10 h-10 text-pink-300 mb-4" />
            <h3 className="text-xl font-bold mb-2 text-white">
              Follow Friends
            </h3>
            <p className="text-gray-400">
              See what your friends are listening to and discover new music.
            </p>
          </div>

          {/* Feature 3 */}
          <div className="bg-gray-900 bg-opacity-50 backdrop-blur-lg p-6 rounded-xl border border-gray-800">
            <TrendingUp className="w-10 h-10 text-blue-400 mb-4" />
            <h3 className="text-xl font-bold mb-2 text-white">
              Trending Albums
            </h3>
            <p className="text-gray-400">
              Stay updated with the most talked about releases.
            </p>
          </div>
        </div>
      </div>
    </div>
    </>
  );
}