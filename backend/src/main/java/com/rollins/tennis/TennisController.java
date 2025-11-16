package com.rollins.tennis;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TennisController {
    private RosterManager rosterManager;
    private MatchRepository matchRepository;
    private StatsService statsService;
    
    public TennisController() {
        this.rosterManager = new RosterManager();
        this.matchRepository = new MatchRepository();
        this.statsService = new StatsService(rosterManager, matchRepository);
        initializeData();
    }
    
    private void initializeData() {
        // Try to load from Excel files first
        try {
            // Get the base path - Excel files are in the project root (parent of backend)
            String basePath = System.getProperty("user.dir");
            java.io.File currentDir = new java.io.File(basePath);
            
            // Try to find the Excel files by checking current directory and parent directories
            java.io.File excelFile = null;
            java.io.File checkDir = currentDir;
            
            // Check up to 3 levels up
            for (int i = 0; i < 3; i++) {
                excelFile = new java.io.File(checkDir, "rawDataMensTennis.xlsx");
                if (excelFile.exists()) {
                    basePath = checkDir.getAbsolutePath();
                    break;
                }
                checkDir = checkDir.getParentFile();
                if (checkDir == null) break;
            }
            
            if (excelFile == null || !excelFile.exists()) {
                throw new Exception("Could not find Excel files. Looking in: " + basePath);
            }
            
            ExcelDataLoader.PlayerMatchData data = ExcelDataLoader.loadData(basePath);
            
            // Add all players from Excel
            for (Player player : data.players) {
                try {
                    rosterManager.addPlayer(player);
                } catch (Exception e) {
                    // Player might already exist, skip
                    System.err.println("Could not add player: " + player.getFullName() + " - " + e.getMessage());
                }
            }
            
            // Add all matches from Excel
            for (Match match : data.matches) {
                matchRepository.addMatch(match);
            }
            
            System.out.println("Loaded " + data.players.size() + " players and " + data.matches.size() + " matches from Excel files");
            
        } catch (Exception e) {
            System.err.println("Error loading Excel data: " + e.getMessage());
            e.printStackTrace();
            // No fallback - only use real data from Excel files
        }
    }
    
    // Player endpoints
    public List<Player> getAllPlayers() {
        return rosterManager.getAllPlayers();
    }
    
    public Player getPlayer(String id) {
        return rosterManager.findPlayerById(id);
    }
    
    public List<Player> searchPlayers(String query) {
        return rosterManager.searchPlayers(query);
    }
    
    // Match endpoints
    public List<MatchData> getAllMatches(String season, String type, String opponent) {
        List<Match> matches;
        if (season != null && !season.equals("All")) {
            Season s = Season.fromYear(season);
            matches = matchRepository.getMatchesBySeason(s);
        } else {
            matches = matchRepository.getAllMatches();
        }
        
        if (type != null && !type.equals("All")) {
            matches = matches.stream()
                .filter(m -> m.getMatchType().equals(type))
                .collect(Collectors.toList());
        }
        
        if (opponent != null && !opponent.trim().isEmpty()) {
            String lowerOpp = opponent.toLowerCase();
            matches = matches.stream()
                .filter(m -> m.getOpponent().toLowerCase().contains(lowerOpp))
                .collect(Collectors.toList());
        }
        
        return matches.stream().map(this::convertMatch).collect(Collectors.toList());
    }
    
    // Stats endpoints
    public Map<String, Object> getPlayerStats(String playerId) {
        Player player = rosterManager.findPlayerById(playerId);
        if (player == null) {
            return Collections.singletonMap("error", "Player not found");
        }
        
        Map<String, Integer> record = statsService.getWinLossByPlayer(player);
        double winPct = statsService.getWinPercentage(player);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("player", player);
        stats.put("wins", record.get("wins"));
        stats.put("losses", record.get("losses"));
        stats.put("winPercentage", winPct);
        return stats;
    }
    
    public Map<String, Object> getOverallStats() {
        Map<String, Integer> record = statsService.getOverallRecord();
        Map<String, Object> stats = new HashMap<>();
        stats.put("wins", record.get("wins"));
        stats.put("losses", record.get("losses"));
        int total = record.get("wins") + record.get("losses");
        stats.put("winPercentage", total > 0 ? (record.get("wins") * 100.0) / total : 0.0);
        return stats;
    }
    
    public Map<String, Object> getSeasonStats(String season) {
        Season s = Season.fromYear(season);
        Map<String, Integer> record = statsService.getSeasonRecord(s);
        Map<String, Object> stats = new HashMap<>();
        stats.put("season", season);
        stats.put("wins", record.get("wins"));
        stats.put("losses", record.get("losses"));
        int total = record.get("wins") + record.get("losses");
        stats.put("winPercentage", total > 0 ? (record.get("wins") * 100.0) / total : 0.0);
        return stats;
    }
    
    private MatchData convertMatch(Match match) {
        MatchData data = new MatchData();
        data.id = match.hashCode() + "";
        data.date = match.getDate().toString();
        data.season = match.getSeason().toString();
        data.type = match.getMatchType();
        data.opponent = match.getOpponent();
        data.score = match.getResult().getScore();
        data.won = match.getResult().isRollinsWon();
        
        if (match instanceof SinglesMatch) {
            SinglesMatch sm = (SinglesMatch) match;
            data.rollinsPlayer = sm.getRollinsPlayer().getFullName();
            data.opponentPlayer = sm.getOpponentPlayer().getFullName();
        } else if (match instanceof DoublesMatch) {
            DoublesMatch dm = (DoublesMatch) match;
            data.rollinsPlayer = dm.getRollinsPlayer1().getFullName() + " / " + dm.getRollinsPlayer2().getFullName();
            data.opponentPlayer = dm.getOpponentPair1() + " / " + dm.getOpponentPair2();
        }
        
        return data;
    }
    
    public static class MatchData {
        public String id;
        public String date;
        public String season;
        public String type;
        public String opponent;
        public String score;
        public boolean won;
        public String rollinsPlayer;
        public String opponentPlayer;
    }
}

