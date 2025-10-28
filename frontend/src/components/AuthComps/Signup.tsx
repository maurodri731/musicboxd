import { useState } from "react";
import SocialLogin from "../UtilComps/SocialLogin";
import InputField from "../UtilComps/InputField";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import api, { AuthResponse } from "../../util/Util";
import axios from 'axios';

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passConfirm, setPassConfirm] = useState("");
  const [displayName, setDisplayname] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [firstName, setFirstname] = useState("");
  const [lastName, setLastname] = useState("");
  const { setUser } = useAuth();//Custom hook that takes care of the Auth Context, will be useful when querying for the users detials
  const navigate = useNavigate();//Used to send the user elsewhere after signing in

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!email || !password) {
      alert("Please fill in all fields");
      return;
    }

    if (password !== passConfirm) {
      alert("The passwords do not match");
      return;
    }

    setIsLoading(true);
    
    try {//use the api function to call register the user (axios)
      const res = await api.post<AuthResponse>("/auth/register", {//the api function is in Util.ts
        email,
        password
      });
      
      setUser(res.data.user);//setUser comes from useAuth() hook, updates the context with the user that has just registered
      console.log(res.data.message);
      navigate("/search-albums");
    } catch (error) {
      setUser(null);//The Authcontext can take a null or a user, if signup fails then just send a null for redundancy
      
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
      <div className="max-w-2xl mx-auto p-8 rounded-lg bg-white shadow-xl">
        <h2 className="text-center text-2xl font-semibold mb-8 text-black">
          Sign up with
        </h2>
        
        <SocialLogin />
        
        <div className="relative my-6 text-center bg-white">
          <span className="relative z-10 bg-white text-blue-600 font-medium text-lg px-4">
            or
          </span>
          <div className="absolute left-0 top-1/2 h-px w-full bg-blue-400"></div>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="flex gap-2">
            <InputField
              type="text"
              placeholder="First Name"
              icon=""
              value={firstName}
              onChange={setFirstname}
              name="firstName"
              addStyle={true}
            />
            <InputField
              type="text"
              placeholder="Last Name"
              icon=""
              value={lastName}
              onChange={setLastname}
              name="lastName"
              addStyle={true}
            />
          </div>
          
          <InputField
            type="text"
            placeholder="Username"
            icon=""
            value={displayName}
            onChange={setDisplayname}
            name="displayName"
            addStyle={false}
          />
          
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
          
          <InputField
            type="password"
            placeholder="Confirm Password"
            icon="lock"
            value={passConfirm}
            onChange={setPassConfirm}
            name="passConfirm"
            addStyle={false}
          />
          
          <button 
            type="submit" 
            className="w-full h-14 text-white text-lg font-medium mt-9 rounded bg-blue-600 hover:bg-blue-900 transition-colors disabled:opacity-50"
            disabled={isLoading}
          >
            {isLoading ? 'Signing up...' : 'Sign Up'}
          </button>
        </form>
        
        <p className="text-center text-lg font-medium mt-7 mb-1 text-black">
          Already have an account?{' '}
          <a href="#" className="text-blue-600 font-medium hover:underline">
            Log in
          </a>
        </p>
      </div>
    </div>
  );
}

