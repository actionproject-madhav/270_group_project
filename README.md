# Rollins Tennis Archive - Full Stack Application
*A Java OOP Project by Madhav Khanal, Stella Fruijtier and Richard Stoiberer*

### What's Running

1. **Backend Server** (Java REST API) - Port 8080
2. **Frontend Server** (React App) - Port 3000

## Access the Application

**Open your browser and go to:**
```
http://localhost:3000
```

## Overview  
**Rollins Tennis Archive** is a Java-based full-stack application that stores and displays **player bios** and **match results from the past three seasons** (singles & doubles).  
The system provides an easy-to-use **React interface** that allows users to browse, search, and analyze player and match data, built to showcase strong **Object-Oriented Programming** principles.

## Project Theme  
The app is inspired by the **Rollins College Tennis Team**, aiming to organize team data and make past results accessible for analysis. It's both a sports management tool and a demonstration of advanced OOP design.

## What You'll See

3 Tabs:

1. **Players Tab**
   - List of 5 players on the left
   - Search box to filter players
   - Click a player to see details (Name, Class, Nationality, UTR)
   - View their career statistics (Wins, Losses, Win %)

2. **Matches Tab**
   - Table of 7 matches
   - Filter by Season: 2022, 2023, 2024
   - Filter by Type: Singles, Doubles
   - Search by Opponent
   - Click a match to see details

3. **Statistics Tab**
   - Overall team record
   - Season breakdown (2022-2024)
   - Individual player records table

## Core Features  
- **Player Bios** – View each player's profile, including name, nationality, class year, and UTR rating.  
- **Match Records** – Access all singles and doubles matches from the last three years.  
- **Filtering** – Filter matches by year, type (Singles/Doubles), or player.  
- **Statistics View** – Automatically calculate win/loss records and performance summaries.  
- **GUI Dashboard (React)** – Clean interface for navigation and data visualization.  
- **Data Persistence** – Uses sample data to simulate historical match results.  

## Object-Oriented Design  

| OOP Concept | Implementation |
|--------------|----------------|
| **Classes & Objects** | `Player`, `Match`, `SinglesMatch`, `DoublesMatch`, `Result`, `Season`, `RosterManager`, `StatsService` |
| **Inheritance** | `SinglesMatch` and `DoublesMatch` extend the abstract class `Match` |
| **Encapsulation** | All fields are private, accessed through getters/setters |
| **Abstraction** | `Match` defines shared structure and behaviour for all match types |
| **Polymorphism** | Collections and GUI methods handle `Match` objects generically |
| **Data Structures** | `ArrayList<Player>`, `ArrayList<Match>`, and `Map<Season, List<Match>>` |
| **Error Handling** | Input validation for years, scores, duplicates, and null values |

## Technology Stack  
- **Language:** Java 17+  
- **UI Framework:** React 
- **IDE:** Eclipse / VS Code  
- **Data Format:** Sample data for players & matches

## Backend API (For Testing)

Test the API directly:
```bash
# Get all players
curl http://localhost:8080/api/players

# Get all matches
curl http://localhost:8080/api/matches

# Get filtered matches
curl "http://localhost:8080/api/matches?season=2024&type=Singles"

# Get statistics
curl http://localhost:8080/api/stats

# Get player stats
curl http://localhost:8080/api/player/P001
```

## Project Structure

```
270_group_project/
├── backend/              # Java REST API
│   ├── src/main/java/com/rollins/tennis/
│   │   ├── Player.java
│   │   ├── Match.java (abstract)
│   │   ├── SinglesMatch.java
│   │   ├── DoublesMatch.java
│   │   ├── Season.java
│   │   ├── Result.java
│   │   ├── RosterManager.java
│   │   ├── MatchRepository.java
│   │   ├── StatsService.java
│   │   ├── TennisController.java
│   │   └── Server.java
│   └── gson.jar
│
└── frontend/             # React App
    ├── src/
    │   ├── App.js
    │   ├── App.css
    │   ├── components/
    │   │   ├── PlayersTab.js
    │   │   ├── MatchesTab.js
    │   │   └── StatsTab.js
    │   └── services/
    │       └── api.js
    └── package.json
```

## Stopping the Servers

```bash
# Find and kill backend
pkill -f "com.rollins.tennis.Server"

# Find and kill frontend
pkill -f "react-scripts"
```

## Features

- Clean black & white UI
- REST API with Java backend
- React frontend
- All OOP principles demonstrated
- Sample data pre-loaded
- Ready to demonstrate
