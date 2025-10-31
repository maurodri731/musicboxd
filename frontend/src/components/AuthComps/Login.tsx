import { useState } from "react";
import SocialLogin from "../UtilComps/SocialLogin";
import InputField from "../UtilComps/InputField";
import { useAuth } from "../../context/AuthContext";
import { Link, useLocation, useNavigate } from "react-router-dom";
import api, { AuthResponse } from "../../util/Util"
import axios from 'axios';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { setUser } = useAuth();//Custom hook that takes care of the Auth Context, will be useful when querying for the users detials
  const navigate = useNavigate();//Used to send the user elsewhere after signing in
  const location = useLocation();
  
  const from = location.state?.from || '/';

const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  
  if (!email || !password) {
    alert("Please fill in all fields");
    return;
  }

  setIsLoading(true);
  
  try {//call the api using the api function from util
    const res = await api.post<AuthResponse>("/auth/login", {
      email,
      password
    });
    
    setUser(res.data.user);//The response is guaranteed to contain a user object, as long as it doesn't return an error
    console.log(res.data.user);
    console.log(res.data.message);
    navigate(from, {replace: true});//Send the user to some other part of the page, this will also re-render the Context with the new user value
  } catch (error) {
    setUser(null);//The Context can have a null user
    
    if (axios.isAxiosError(error)) {
      console.error(error.response?.data);
      const errorMessage = error.response?.data?.message || 'Login failed. Please check your credentials.';
      alert(errorMessage);
    } else {
      console.error(error);
      alert('An unexpected error occurred. Please try again.');
    }
  } finally {
    setIsLoading(false);
  }
};

  return (
    <div className="pt-24 px-4">
      <div className="max-w-md mx-auto p-8 rounded-lg bg-white shadow-xl">
        <h2 className="text-center text-2xl font-semibold mb-8 text-black">
          Log in with
        </h2>
        
        <SocialLogin />{/*This will carry any third party authentication */}
        
        <div className="relative my-6 text-center bg-white">
          <span className="relative z-10 bg-white text-blue-600 font-medium text-lg px-4">
            or
          </span>
          <div className="absolute left-0 top-1/2 h-px w-full bg-blue-400"></div>
        </div>
        
        <form onSubmit={handleSubmit}>
          <InputField 
            type="email" 
            placeholder="Email address" 
            icon="mail"
            value={email}
            onChange={setEmail}
            name="email"
            addStyle={false}
          />
          <InputField 
            type="password" 
            placeholder="Password" 
            icon="lock"
            value={password}
            onChange={setPassword}
            name="password"
            addStyle={false}
          />
          
          <a 
            href="#" 
            className="block w-fit -mt-2 text-blue-600 font-medium hover:underline"
          >
            Forgot password?
          </a>
          
          <button 
            type="submit" 
            className="w-full h-14 text-white text-lg font-medium mt-9 rounded bg-blue-600 hover:bg-blue-900 transition-colors disabled:opacity-50"
            disabled={isLoading}
          >
            {isLoading ? 'Logging in...' : 'Log In'}
          </button>
        </form>
        
        <p className="text-center text-lg font-medium mt-7 mb-1 text-black">
          Don't have an account?{' '}
          <Link to="/auth?mode=sign-up" className="text-blue-600 font-medium hover:underline">
            Sign Up
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
