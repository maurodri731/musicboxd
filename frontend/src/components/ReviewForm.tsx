import { PopAlbum } from "../util/Util"

interface Props{
    album: PopAlbum | null;
}
export default function ReviewForm({album}: Props ) {
    return(
        <div className="flex flex-col w-full h-full bg-gray-800 rounded-lg overflow-hidden">
            <div className="flex-shrink-0 flex flex-col sm:flex-row gap-2 sm:gap-4 p-3 sm:p-6 md:p-8">
                <div className="flex w-full sm:w-[40%] items-center justify-center">
                    <div className="w-32 h-32 sm:w-40 sm:h-40 md:w-48 md:h-48 lg:w-56 lg:h-56 rounded-lg shadow-xl overflow-hidden shadow-gray-700">
                        <img 
                        src={album?.imageUrl} 
                        alt={album?.albumName}
                        className="w-full h-full object-cover transition-all duration-300"
                        />
                    </div>
                </div>
                <div className="flex flex-col w-full sm:w-[60%] items-center justify-center">
                    <p className="text-white text-center font-bold text-lg sm:text-xl md:text-2xl lg:text-3xl break-words px-2">{album?.albumName}</p>
                    <p className="text-white text-center text-base sm:text-lg md:text-xl lg:text-2xl break-words px-2">{album?.artistName}</p>
                </div>
            </div>
            <div className="flex-1 p-3 sm:p-4 md:p-6 lg:p-8 overflow-hidden flex flex-col min-h-0">
                <textarea 
                    placeholder="Enter your review" 
                    className="w-full h-full px-3 py-2 sm:px-4 sm:py-3 md:px-5 md:py-4 rounded-lg resize-none bg-gray-700 text-white text-sm sm:text-base md:text-lg" 
                />
            </div>
        </div>
    );
}
