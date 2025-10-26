interface Props {
    title: string,
    artist: string,
    cover: string
    onClick: () => void;
}
export default function AlbumCard({title, artist, cover, onClick}:Props) {
    return (
        <div onClick={onClick} className="flex sm:p-1 lg:p-4 md:p-2 mb-12 rounded-lg bg-blue-400 hover:bg-purple-500 transition-colors duration-300 group">
            <div className="w-72 h-72 group cursor-pointer transition-transform duration-200 ease-in-out group-hover:scale-105">
              <div className="aspect-square bg-gray-800 rounded-lg overflow-hidden mb-3 relative">
                <img 
                  src={cover} 
                  alt={title}
                  className="w-full h-full object-cover transition-all duration-300"
                />
              </div>
              <h4 className="text-white font-semibold truncate mt-4">{title}</h4>
              <p className="text-gray-400 text-sm truncate">{artist}</p>
            </div>
          </div>
    )
}