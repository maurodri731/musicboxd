import { useState, useEffect } from "react";
import { useSearchParams } from 'react-router-dom';
import Login from "../components/AuthComps/Login.tsx"
import Signup from '../components/AuthComps/Signup.tsx';
import NavbarComp from '../components/UtilComps/NavbarComp';
import BackgroundGradient from '../components/UtilComps/BackgroundGradient';

const AuthPage = () => {
  const [searchParams] = useSearchParams();
  const [mode, setMode] = useState('login');

    useEffect(() => {
    const urlMode = searchParams.get('mode');
    if (urlMode === 'sign-up') {
      setMode('sign-up');
    } else if (urlMode === 'login') {
      setMode('login');
    }
  }, [searchParams]);

  return (
    <>
      <BackgroundGradient gradient="linear-gradient(135deg, #ffffff 0%, #581c87 90%)"/>
      <NavbarComp/>
      {mode === 'login' ? (<Login />) : (<Signup />)}
    </>
  );
}

export default AuthPage;