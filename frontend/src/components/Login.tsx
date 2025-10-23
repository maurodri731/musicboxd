import { useState } from "react";
import SocialLogin from "./SocialLogin";
import InputField from "./InputField";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!email || !password) {
      alert("Please fill in all fields");
      return;
    }

    setIsLoading(true);
    
    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: email,
          password: password
        })
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Login successful:', data);
      } else {
        console.error('Login failed:', response.statusText);
        alert('Login failed. Please check your credentials.');
      }
    } catch (error) {
      console.error('Error during login:', error);
      alert('An error occurred. Please try again.');
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
        
        <SocialLogin />
        
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
          <a href="#" className="text-blue-600 font-medium hover:underline">
            Sign up
          </a>
        </p>
      </div>
    </div>
  );
};

export default Login;
