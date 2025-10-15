import { useState } from "react";
import SocialLogin from "./SocialLogin";
import InputField from "./InputField";
import { Container } from "react-bootstrap";

export default function Signup(){
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

    if(password !== passConfirm) {
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
        // Handle successful login (e.g., redirect, store token, etc.)
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
    <Container style={{padding: "100px 0 0 0"}}>
      <div className="login-container" style={{width: '40vw'}}>
          <h2 className="form-title" color="#000">Sign up with</h2>
          <SocialLogin />
          <p className="separator"><span>or</span></p>
          <form onSubmit={handleSubmit} className="login-form">
              <InputField
              type="firstName"
              placeholder="First Name"    
              icon=""
              value={firstName}
              onChange={setFirstname}
              name="firstName"
              addStyle={true}
              />
              <InputField
              type="lastName"
              placeholder="Last Name"    
              icon=""
              value={lastName}
              onChange={setLastname}
              name="lastName"
              addStyle={true}
              />
              <InputField
              type="displayName"
              placeholder="Username"    
              icon=""
              value={displayName}
              onChange={setDisplayname}
              name="displayName"
              addStyle={true}
              />
              <InputField 
              type="email" 
              placeholder="Email address" 
              icon="mail"
              value={email}
              onChange={setEmail}
              name="email"
              addStyle={true}
              />
              <InputField 
              type="password" 
              placeholder="Password" 
              icon="lock"
              value={password}
              onChange={setPassword}
              name="password"
              addStyle={true}
              />
              <InputField
              type="passConfirm"
              placeholder="Confirm Password"    
              icon="lock"
              value={passConfirm}
              onChange={setPassConfirm}
              name="passConfirm"
              addStyle={true}
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
    </Container>
  )
}