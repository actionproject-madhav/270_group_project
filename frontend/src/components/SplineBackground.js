import React from 'react';
import Spline from '@splinetool/react-spline';
import './SplineBackground.css';

export default function SplineBackground() {
  return (
    <div className="spline-background">
      <Spline
        scene="https://prod.spline.design/gSlo7bIqiYoQjrBv/scene.splinecode"
        className="spline-canvas"
      />
    </div>
  );
}

