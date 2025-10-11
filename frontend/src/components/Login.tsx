// Updated Login.tsx with POST request
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
      const response = await fetch('/api/login', {
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
        // Handle successful login (e.g., redirect, store token, etc.)
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
    <div className="login-container">
      <h2 className="form-title" color="#000">Log in with</h2>
      <SocialLogin />
      <p className="separator"><span>or</span></p>
      <form onSubmit={handleSubmit} className="login-form">
        <InputField 
          type="email" 
          placeholder="Email address" 
          icon="mail"
          value={email}
          onChange={setEmail}
          name="email"
        />
        <InputField 
          type="password" 
          placeholder="Password" 
          icon="lock"
          value={password}
          onChange={setPassword}
          name="password"
        />
        <a href="#" className="forgot-password-link">Forgot password?</a>
        <button 
          type="submit" 
          className="login-button"
          disabled={isLoading}
        >
          {isLoading ? 'Logging in...' : 'Log In'}
        </button>
      </form>
      <p className="signup-prompt">
        Don't have an account? <a href="#" className="signup-link">Sign up</a>
      </p>
    </div>
  )
}

export default Login;