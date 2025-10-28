# Backend - Java REST API

REST API for the Rollins Tennis Archive.

## Running

```bash
# Compile
mkdir -p out
javac -cp gson.jar src/main/java/com/rollins/tennis/*.java -d out/

# Run
java -cp gson.jar:out/ com.rollins.tennis.Server
```

## API Endpoints

### Get All Players
```
GET /api/players
Response: Array of Player objects
```

### Get All Matches
```
GET /api/matches
Query params:
  - season: 2022, 2023, 2024, or All
  - type: Singles, Doubles, or All
  - opponent: Filter by opponent name

Response: Array of Match objects
```

### Get Statistics
```
GET /api/stats?season=2024
Query params:
  - season: Optional, specific year or all

Response: { wins, losses, winPercentage }
```

### Get Player Statistics
```
GET /api/player/{playerId}
Response: { player, wins, losses, winPercentage }
```

## Architecture

- **Domain Classes**: Player, Match (abstract), SinglesMatch, DoublesMatch, Result, Season
- **Managers**: RosterManager, MatchRepository, StatsService
- **REST Controller**: TennisController
- **HTTP Server**: Server (com.sun.net.httpserver)

## OOP Features

✅ Abstract Match class with concrete subclasses  
✅ Encapsulation (private fields, public getters)  
✅ Inheritance (SinglesMatch, DoublesMatch extend Match)  
✅ Polymorphism (List<Match> holds subclasses)  
✅ Data structures (ArrayList, HashMap)  

## Sample Data

Includes 5 players and 7 matches across 2022-2024 seasons.

