import React, { createContext, useContext, useState, useEffect, ReactNode} from 'react';
import api from "../util/Util"
import { User } from "../util/Util"
import { AuthResponse } from '../util/Util';
import axios from "axios";

interface AuthContextType {
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
        api.get<AuthResponse>("/auth/me")
        .then((res) => {
            setUser(res.data.user as User)
            console.log(res.data.message)
        })
        .catch((error) => {
            setUser(null);
            console.log(axios.isAxiosError(error));
            console.log(error.response?.data);
        })
        .finally(() => setLoading(false));
    },[]);

    const logout = async () => {
        await api.post("auth/logout");
        setUser(null);
    };

    const login = async () => {
        await api.post<AuthResponse>("auth/login")
        .then(res => setUser(res.data.user as User))
        .catch(() => setUser(null))
        .finally(() => setLoading(false));
    }
    return (
        <AuthContext.Provider value={{ user, loading, login, logout, setUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if(!context){
        throw new Error("useAuth myst be used within an Auth Provider");
    }
    return context;
}
