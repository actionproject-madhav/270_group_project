import React from 'react';
import Spline from '@splinetool/react-spline';
import './SplineBackground.css';

export default function SplineBackground() {
  // Tennis court Spline scene URL - update this with your tennis court scene
  const TENNIS_COURT_SCENE = process.env.REACT_APP_SPLINE_SCENE_URL || 
    'https://prod.spline.design/YOUR_TENNIS_COURT_SCENE_URL/scene.splinecode';
  
  return (
    <div className="spline-background">
      <Spline
        scene={TENNIS_COURT_SCENE}
        className="spline-canvas"
      />
    </div>
  );
}

