import { useEffect } from "react";
import { useNavigate, useLocation, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";


//Block non-signed in users from entering pages that require it
//Why is in context you might ask?
//Well, it will consume that context so why not?
export default function AuthGuard() {
    const { user } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        if(user == null){
            navigate('/auth?mode=login', {state: {from: location.pathname}})
        }
    }, [user, navigate, location]);

    if(user === null) {
        return <div>Loading...</div>
    }

    return <Outlet />;
}