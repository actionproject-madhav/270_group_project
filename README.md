# 🎾 Rollins Tennis Archive  
*A Java OOP Project by Madhav Khanal, Stella Fruijtier and Richard Stoiberer

## 📘 Overview  
**Rollins Tennis Archive** is a Java-based desktop application that stores and displays **player bios** and **match results from the past three seasons** (singles & doubles).  
The system provides an easy-to-use **JavaFX interface** that allows users to browse, search, and analyze player and match data — built to showcase strong **Object-Oriented Programming** principles.

---

## 💡 Project Theme  
The app is inspired by the **Rollins College Tennis Team**, aiming to organize team data and make past results accessible for analysis. It’s both a sports management tool and a demonstration of advanced OOP design.

---

## 🧩 Core Features  
- **Player Bios** – View each player’s profile, including name, nationality, class year, and UTR rating.  
- **Match Records** – Access all singles and doubles matches from the last three years.  
- **Filtering** – Filter matches by year, type (Singles/Doubles), or player.  
- **Statistics View** – Automatically calculate win/loss records and performance summaries.  
- **GUI Dashboard (React)** – Clean interface for navigation and data visualization.  
- **Data Persistence** – Uses sample data (JSON/CSV) to simulate historical match results.  

---

## 🧠 Object-Oriented Design  

| OOP Concept | Implementation |
|--------------|----------------|
| **Classes & Objects** | `Player`, `Match`, `SinglesMatch`, `DoublesMatch`, `Result`, `Season`, `RosterManager`, `StatsService` |
| **Inheritance** | `SinglesMatch` and `DoublesMatch` extend the abstract class `Match` |
| **Encapsulation** | All fields are private, accessed through getters/setters |
| **Abstraction** | `Match` defines shared structure and behavior for all match types |
| **Polymorphism** | Collections and GUI methods handle `Match` objects generically |
| **Interface (Optional)** | `StatsStrategy` interface for flexible stat calculations |
| **Data Structures** | `ArrayList<Player>`, `ArrayList<Match>`, and `Map<Season, List<Match>>` |
| **Error Handling** | Input validation for years, scores, duplicates, and null values |

---

## 🖥️ Technology Stack  
- **Language:** Java 17+  
- **GUI Framework:** JavaFX  
- **IDE Recommended:** IntelliJ IDEA / Eclipse / VS Code  
- **Data Format:** JSON or CSV (sample data for players & matches)  
