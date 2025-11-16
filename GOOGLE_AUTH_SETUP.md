# Google OAuth Setup Guide

## Step 1: Create Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Click "Select a project" → "New Project"
3. Name your project (e.g., "Rollins Tennis Archive")
4. Click "Create"

## Step 2: Enable Google+ API / Google Identity Services

1. In Google Cloud Console, go to "APIs & Services" → "Library"
2. Search for "Google Identity Services API" or "Google+ API"
3. Click "Enable"

## Step 3: Create OAuth 2.0 Credentials

1. Go to "APIs & Services" → "Credentials"
2. Click "Create Credentials" → "OAuth client ID"
3. If prompted, configure the OAuth consent screen first:
   - Choose "External" (unless you have a Google Workspace)
   - Fill in:
     - App name: "Rollins Tennis Archive"
     - User support email: Your email
     - Developer contact: Your email
   - Click "Save and Continue"
   - Add scopes (optional): `email`, `profile`
   - Add test users (your email) if in testing mode
   - Click "Save and Continue"

4. Create OAuth Client ID:
   - Application type: **Web application**
   - Name: "Rollins Tennis Web Client"
   - **Authorized JavaScript origins:**
     - `http://localhost:3000`
     - `http://localhost:8080` (if needed)
   - **Authorized redirect URIs:**
     - `http://localhost:3000/auth/callback`
     - `http://localhost:3000` (if using popup)
   - Click "Create"

5. **Copy your credentials:**
   - **Client ID**: Looks like `123456789-abcdefghijklmnop.apps.googleusercontent.com`
   - **Client Secret**: Click "Show" to reveal (looks like `GOCSPX-xxxxxxxxxxxxx`)

## Step 4: Add Credentials to Your Project

### Option A: Environment Variables (Recommended)

Create a `.env` file in the `frontend` directory:

```bash
REACT_APP_GOOGLE_CLIENT_ID=your-client-id-here.apps.googleusercontent.com
REACT_APP_GOOGLE_CLIENT_SECRET=your-client-secret-here
```

### Option B: Config File

Create `frontend/src/config/auth.js`:

```javascript
export const GOOGLE_CLIENT_ID = 'your-client-id-here.apps.googleusercontent.com';
export const GOOGLE_CLIENT_SECRET = 'your-client-secret-here';
```

## Important Notes:

⚠️ **Never commit your Client Secret to version control!**
- Add `.env` to `.gitignore`
- Client Secret should only be used on the backend (server-side)
- Frontend only needs the Client ID

## For Development:

- Use `http://localhost:3000` in authorized origins
- For production, add your production domain

## Security Best Practices:

1. **Client ID** - Safe to use in frontend (public)
2. **Client Secret** - Keep secret! Only use on backend
3. Add `.env` to `.gitignore`
4. Never expose Client Secret in client-side code

