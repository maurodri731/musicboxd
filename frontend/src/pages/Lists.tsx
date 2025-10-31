import React, { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";


export default function Lists() {
const { user, loading } = useAuth();//use this to check if there is a user signed-in, if there isn't, then send them to the signup page
let newUser = null;
    const load = () => {
        if(loading)
            newUser = user;
    }
    return (
        <h1>Hello there</h1>
    );
}