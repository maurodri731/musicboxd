import { motion } from 'framer-motion';
import "../style/Landing.css";
import { useState, useEffect } from "react";
import { useSearchParams } from 'react-router-dom';
import Login from "../components/Login"
import Signup from '../components/Signup';
import NavbarComp from '../components/NavbarComp';

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
      <motion.div
        initial={{ opacity: 0 }}
        animate={{
          opacity: 1,
          transition: { duration: 0.6, delay: 0.3 }
        }}
        style={{
          minHeight: '100vh',
          position: 'relative',
          overflow: 'hidden',
          background: 'linear-gradient(135deg, #ffffff 0%, #581c87 90%)',
        }}
      >
      <NavbarComp/>
      {mode === 'login' ? (<Login />) : (<Signup />)}
      </motion.div>
    </>
  );
}

export default AuthPage;