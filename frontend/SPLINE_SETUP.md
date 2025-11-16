# Spline Tennis Court Background Setup

## To Add Your Tennis Court Spline Scene:

1. Get your Spline scene URL from Spline (it should look like):
   ```
   https://prod.spline.design/XXXXXXXXXXXX/scene.splinecode
   ```

2. Update the scene URL in one of these ways:

   **Option A: Update directly in SplineBackground.js**
   - Edit `frontend/src/components/SplineBackground.js`
   - Replace `YOUR_TENNIS_COURT_SCENE_URL` with your actual scene URL

   **Option B: Use environment variable**
   - Add to `frontend/.env`:
   ```
   REACT_APP_SPLINE_SCENE_URL=https://prod.spline.design/YOUR_SCENE_ID/scene.splinecode
   ```

## Current Setup:
The component is ready to use your tennis court scene. Just provide the Spline URL!

