import React, { createContext, useContext, useState, useEffect, ReactNode} from 'react';
import api from "../util/Util"
import { User } from "../util/Util"
import { AuthResponse } from '../util/Util';
import axios from "axios";

interface AuthContextType {//List all of the available details and functions for all of the pages within the provider
    user: User | null;
    loading: boolean;
    logout: () => Promise<void>;
    login: () => Promise<void>;
    setUser: React.Dispatch<React.SetStateAction<User | null>>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{children:ReactNode}> = ({children}) => {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get<AuthResponse>("/auth/me")//When the page is initally rendered, check if there are any cookies, so that the user doesn't
        .then((res) => {//have to sign in again
            setUser(res.data.user as User)
            console.log(res.data.message);
            console.log(res.data.user);
        })
        .catch((error) => {
            setUser(null);
            console.log(axios.isAxiosError(error));
            console.log(error.response?.data);
        })
        .finally(() => setLoading(false));
    },[]);

    const logout = async () => {//Will be used to delete the cookies from the browser
        await api.post("auth/logout");
        setUser(null);
    };

    const login = async () => {//Might be used as the refactoring of the Login call in Login.tsx, haven't decided yet
        await api.post<AuthResponse>("auth/login")
        .then(res => setUser(res.data.user as User))
        .catch(() => setUser(null))
        .finally(() => setLoading(false));
    }
    return (//Make the functions and variables available to all of the children of AuthProvider
        <AuthContext.Provider value={{ user, loading, login, logout, setUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {//used to interact with the AuthContext itself.
    const context = useContext(AuthContext);
    if(!context){
        throw new Error("useAuth myst be used within an Auth Provider");
    }
    return context;
}
