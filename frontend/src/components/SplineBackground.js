import React, { useState } from 'react';
import Spline from '@splinetool/react-spline';
import './SplineBackground.css';

export default function SplineBackground() {
  const [splineError, setSplineError] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  
  // Tennis court Spline scene URL - update with your tennis court scene URL
  // Get your scene URL from Spline: https://spline.design
  // Format: https://prod.spline.design/YOUR_SCENE_ID/scene.splinecode
  const TENNIS_COURT_SCENE = process.env.REACT_APP_SPLINE_SCENE_URL || 
    null; // Set to null to use fallback, or provide your tennis court scene URL here
  
  const handleError = (error) => {
    console.error('Spline loading error:', error);
    setSplineError(true);
    setIsLoading(false);
  };
  
  const handleLoad = () => {
    setIsLoading(false);
    setSplineError(false);
  };
  
  // Fallback: Green tennis court gradient background if no URL or on error
  if (!TENNIS_COURT_SCENE || splineError) {
    return (
      <div className="spline-background tennis-court-fallback">
        <div className="tennis-court-pattern"></div>
      </div>
    );
  }
  
  return (
    <div className="spline-background">
      <Spline
        scene={TENNIS_COURT_SCENE}
        className="spline-canvas"
        onError={handleError}
        onLoad={handleLoad}
      />
      {isLoading && (
        <div style={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          color: 'rgba(255, 255, 255, 0.5)',
          fontSize: '14px',
          zIndex: 1
        }}>
          Loading 3D scene...
        </div>
      )}
    </div>
  );
}

