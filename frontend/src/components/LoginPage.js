import React, { useState } from 'react';
import { GoogleOAuthProvider } from '@react-oauth/google';
import GoogleLogin from './GoogleLogin';
import SplineBackground from './SplineBackground';
import './LoginPage.css';

const GOOGLE_CLIENT_ID = process.env.REACT_APP_GOOGLE_CLIENT_ID || '502020189076-e1rn4da1t8ivs3h6srf0a84kne2djq9c.apps.googleusercontent.com';

function LoginPage({ onLoginSuccess }) {
  const [error, setError] = useState(null);

  const handleLoginSuccess = (userData) => {
    console.log('Login successful:', userData);
    // Store user data in localStorage
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('isAuthenticated', 'true');
    onLoginSuccess(userData);
  };

  const handleLoginError = (error) => {
    console.error('Login error:', error);
    setError('Failed to sign in. Please try again.');
  };

  return (
    <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
      <div className="login-page">
        <SplineBackground />
        <div className="login-container">
          <div className="login-card">
            <h1 className="login-title">ROLLINS TENNIS ARCHIVE</h1>
            <p className="login-subtitle">Sign in to access player statistics and match records</p>
            
            {error && (
              <div className="login-error">
                {error}
              </div>
            )}

            <div className="login-button-wrapper">
              <GoogleLogin 
                onSuccess={handleLoginSuccess}
                onError={handleLoginError}
              />
            </div>

            <p className="login-footer">
              By signing in, you agree to access the Rollins Tennis Archive
            </p>
          </div>
        </div>
      </div>
    </GoogleOAuthProvider>
  );
}

export default LoginPage;

