import { PopAlbum } from "../../util/Util"
import React, { useState } from "react";
import api from "../../util/Util";
import { useAuth } from "../../context/AuthContext";
import { LoadingSpinner } from "../UtilComps/LoadingSpinner";
import RatingComp from "../UtilComps/RatingComp";

interface Props{
    album: PopAlbum | null;
}
export default function ReviewForm({album}: Props ) {
    const [review, setReview] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);
    const [rating, setRating] = useState<number>(0);
    const { user } = useAuth();

    const handleSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
        console.log(user?.id);
        setLoading(true);
        try{
            const response = await api.post("/api/review", {
                user_id: user?.id,
                text: review, 
                rating, 
                album,
            });

            console.log(response)
        } catch (error){
            console.log("Error submitting the review", error);
        } finally {
            setLoading(false);
        }
    }

    return(
        <div className="flex flex-col w-full h-full bg-gray-800 rounded-lg overflow-auto">
            <div className="flex-1 flex flex-col sm:flex-row gap-3 sm:gap-4 md:gap-6 p-3 sm:p-4 md:p-6 lg:p-8 overflow-hidden min-h-0">
                <div className="flex-shrink-0 flex flex-col gap-2 sm:gap-3 w-full sm:w-auto">
                    <div className="w-32 h-32 sm:w-40 sm:h-40 md:w-48 md:h-48 lg:w-56 lg:h-56 mx-auto sm:mx-0 rounded-lg shadow-xl overflow-hidden shadow-gray-700">
                        <img 
                        src={album?.imageUrl} 
                        alt={album?.albumName}
                        className="w-full h-full object-cover transition-all duration-300"
                        />
                    </div>
                    <div className="flex flex-col gap-1 max-w-[200px] mx-auto sm:mx-0">
                        <p className="text-white text-center sm:text-left font-bold text-lg sm:text-xl md:text-2xl break-words">{album?.albumName}</p>
                        <p className="text-white text-center sm:text-left text-base sm:text-lg md:text-xl break-words">{album?.artistName}</p>
                    </div>
                </div>
                <div className="flex-1 overflow-hidden flex flex-col min-h-0">
                    <textarea 
                        placeholder="Enter your review" 
                        className="w-full h-full px-3 py-2 sm:px-4 sm:py-3 md:px-5 md:py-4 rounded-lg resize-none bg-gray-700 text-white text-sm sm:text-base md:text-lg" 
                        value={review}
                        onChange={(e) => setReview(e.target.value)}
                    />
                </div>
            </div>
            <div className="flex-shrink-0 flex flex-col sm:flex-row gap-[10%] md:gap-[40%] items-center justify-start px-3 py-3 sm:px-4 sm:py-4 md:px-6 md:py-4 border-t border-gray-700">
                <RatingComp onRatingChange={(value) => setRating(value)}/>{/*Track the rating*/}
                { !loading ? 
                (<button 
                    className="px-4 py-2 sm:px-6 sm:py-2 md:px-8 md:py-3 bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700 text-white font-semibold rounded-lg shadow-lg transition-all duration-300 transform hover:scale-105 text-sm sm:text-base md:text-lg"
                    onClick={handleSubmit}
                >
                    Save
                </button>)
                : (
                    <LoadingSpinner size="sm" color="purple"/>
                )
                }
            </div>
        </div>
    );
}
