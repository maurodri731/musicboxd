import { useState, useEffect } from "react";
import { useSearchParams } from 'react-router-dom';
import Login from "../components/Login"
import Signup from '../components/Signup';
import NavbarComp from '../components/NavbarComp';
import BackgroundGradient from '../components/BackgroundGradient';

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