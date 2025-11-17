import React from 'react';
import './SplineBackground.css';

export default function SplineBackground() {
  return (
    <div 
      className="spline-background tennis-image-background"
      style={{
        backgroundImage: 'url(/images/tennis.jpg)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        backgroundAttachment: 'fixed'
      }}
    />
  );
}

