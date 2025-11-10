import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import api, { PageReviewResponse, ReviewResponse } from "../util/Util";

export default function Lists() {
const { user, loading } = useAuth();//use this to check if there is a user signed-in, if there isn't, then send them to the signup page
const [reviews, setReviews] = useState<ReviewResponse[]>([]);

useEffect(() => {
    api.get<PageReviewResponse>(`api/get-user-reviews/${user?.id}`)
    .then(response => {
        setReviews(response.data.content);
        console.log(response.data.content);
    })
    .catch(error => console.log(error))
}, []);

    return (
        <h1>hello there</h1>
    );
}