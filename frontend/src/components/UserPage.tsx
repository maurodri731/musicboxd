import { useEffect, useState } from 'react'
import NavbarComp from './UtilComps/NavbarComp';
import { useAuth } from '../context/AuthContext';

interface UserType{
  displayName: string;
  email: string;
}

const initialState = {displayName: "", email: ""};

const UserPage = () => {
    const [userPage, setUserPage] = useState<UserType>(initialState);
    const [shouldFetch, setShouldFetch] = useState<Boolean>(true);
    const { user } = useAuth();

    useEffect(() => {
        console.log(user);
        fetch("http://localhost:8080/api/user-profile")
        .then(response => response.json())
        .then(data => {
            console.log(data)
            setUserPage(data)
            setShouldFetch(false)
        })
    }, [shouldFetch]);
  return (
    <div>
      <NavbarComp/>
      {userPage ? <h1 className="text" key={userPage.displayName}>{userPage.displayName}</h1> : <h1 className='text'>LOADING...</h1>}
      <h1>{user?.displayName}</h1>
    </div>

  );
}

export default UserPage