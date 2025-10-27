interface Props {
    title: string,
    artist: string,
    cover: string
    onClick: () => void;
}
export default function AlbumCard({title, artist, cover, onClick}:Props) {
    return (
        <div onClick={onClick} className="flex p-2 sm:p-3 md:p-2 lg:p-4 rounded-lg bg-blue-400 hover:bg-purple-500 transition-colors duration-300 group w-full max-w-sm mx-auto cursor-pointer">
            <div className="w-full">
              <div className="bg-gray-800 rounded-lg overflow-hidden relative aspect-square transition-transform duration-200 ease-in-out group-hover:scale-105">
                <img 
                  src={cover} 
                  alt={title}
                  className="w-full h-full object-cover transition-all duration-300"
                />
              </div>
              <h4 className="text-white font-semibold truncate mt-3">{title}</h4>
              <p className="text-white text-sm truncate">{artist}</p>
            </div>
          </div>
    )
}