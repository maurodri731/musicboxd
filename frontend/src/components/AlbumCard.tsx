interface Props {
    title: string,
    artist: string,
    cover: string
}
export default function AlbumCard({title, artist, cover}:Props) {
    return (
        <div className='sm:p-1 lg:p-4 md:p-2 mb-12 rounded-lg bg-blue-400'>
            <div className="w-72 h-72 group cursor-pointer">
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