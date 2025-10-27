import { useState } from "react";
import SocialLogin from "../UtilComps/SocialLogin";
import InputField from "../UtilComps/InputField";

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passConfirm, setPassConfirm] = useState("");
  const [displayName, setDisplayname] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [firstName, setFirstname] = useState("");
  const [lastName, setLastname] = useState("");

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
    
    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
          'Access-Control-Allow-Origin': 'http://localhost:8080',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          password: password,
          displayName: displayName,
          firstName: firstName,
          lastName: lastName,
        })
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Sign-up successful:', data);
      } else {
        console.error('Sign-up failed:', response.statusText);
        alert('Sign-up failed. Please check your credentials.');
      }
    } catch (error) {
      console.error('Error during sign-up:', error);
      alert('An error occurred. Please try again.');
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

