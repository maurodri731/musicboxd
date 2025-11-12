import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import api, { AlbumResponse, PageReviewResponse, ReviewResponse } from "../util/Util";
import Modal from "../components/UtilComps/Modal";
import { LoadingSpinner } from "../components/UtilComps/LoadingSpinner";
import AlbumCard from "../components/AlbumSearchComps/AlbumCard";
import ReviewForm from "../components/AlbumSearchComps/ReviewForm";
import NavbarComp from "../components/UtilComps/NavbarComp";

//this component might end up looking a lot like the search-albums component, I might refactor them into one at some point
//Or it might be better to refactor the way the Albums are rendered? Since they are the same it might be worth simplifying them into a
//single component?
export default function UserReviews(){
    const { user } = useAuth();
    const [reviews, setReviews ] = useState<ReviewResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [modalState, setModalState] = useState<{ isOpen: boolean; selectedAlbum:AlbumResponse | null,  selectedReview: ReviewResponse | null}>
        ({isOpen:false, selectedAlbum:null, selectedReview:null});

    useEffect(() => {//load the user's reviews when the page is initially loaded
        setLoading(true);
        api.get<PageReviewResponse>(`api/get-user-reviews/${user?.id}`)
        .then(response => {
            setReviews(response.data.content);
            console.log(response.data.content);
        })
        .catch(error => console.log(error))
        .finally(() => setLoading(false))
    }, []);

    const handleClick = (review: ReviewResponse) => {//handles the storing of the album details for the modal to use
        console.log("Album clicked");
        setModalState({ isOpen: true, selectedAlbum:review.album, selectedReview:review});
    };

    return (
        <>
            <NavbarComp/>
            <div className="max-w-6xl mx-auto px-4 md:px-6 lg:px-8">
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 pb-12">
                    {loading ? (
                    <LoadingSpinner size="lg" color="purple"/>//Loading animation
                    ) : reviews.length === 0 ? (//check if there are any results from the search query, indicate if there weren't any
                    <div className="col-span-full text-center py-12">
                        <p className="text-gray-400 text-xl">No albums found</p>
                    </div>
                    ) : (
                    reviews.map((review) => (//output the albums found
                        <AlbumCard 
                        key={review.album.albumId} 
                        onClick={() => handleClick(review)} 
                        title={review.album.albumName} 
                        artist={review.album.artistName} 
                        cover={review.album.imageUrl} 
                        /> 
                    ))
                    )}
                </div>
            </div>
            <Modal isOpen={modalState.isOpen} onClose={() => setModalState({isOpen:false, selectedAlbum:null, selectedReview:null})}>
                <ReviewForm album={modalState.selectedAlbum} existingReview={modalState.selectedReview}/>
            </Modal>
        </>
    );
}