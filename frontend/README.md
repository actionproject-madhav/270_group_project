# Frontend - React Application

React frontend for the Rollins Tennis Archive.

## Running

```bash
# Install dependencies (first time only)
npm install

# Start the development server
npm start
```

Opens at http://localhost:3000

## Components

### PlayersTab
- Displays list of players
- Search functionality
- Shows player details and statistics

### MatchesTab
- Table of matches
- Filter by season, type, opponent
- Match details panel

### StatsTab
- Overall team statistics
- Season breakdown
- Individual player records

## API Integration

Uses `/src/services/api.js` for backend communication.

Endpoints:
- GET `/api/players`
- GET `/api/matches` (with filters)
- GET `/api/stats`
- GET `/api/player/{id}`

## Styling

- **Theme**: Black and white
- **Typography**: Arial
- **Design**: Minimalist, professional
- **No** emojis or rainbow colors

## Features

- Search players by name
- Filter matches by season, type, opponent
- View detailed statistics
- Responsive layout
- Clean, professional UI

