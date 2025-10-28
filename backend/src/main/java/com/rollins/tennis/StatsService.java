package com.rollins.tennis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsService {
    private MatchRepository matchRepository;
    
    public StatsService(RosterManager rosterManager, MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }
    
    public Map<String, Integer> getWinLossByPlayer(Player player) {
        Map<String, Integer> stats = new HashMap<>();
        int wins = 0;
        int losses = 0;
        
        for (Match match : matchRepository.getAllMatches()) {
            List<Player> participants = match.getParticipants();
            boolean participated = participants.contains(player);
            
            if (participated) {
                if (match.getWinner()) {
                    wins++;
                } else {
                    losses++;
                }
            }
        }
        
        stats.put("wins", wins);
        stats.put("losses", losses);
        return stats;
    }
    
    public Map<String, Integer> getWinLossByPlayerAndSeason(Player player, Season season) {
        Map<String, Integer> stats = new HashMap<>();
        int wins = 0;
        int losses = 0;
        
        for (Match match : matchRepository.getMatchesBySeason(season)) {
            List<Player> participants = match.getParticipants();
            boolean participated = participants.contains(player);
            
            if (participated) {
                if (match.getWinner()) {
                    wins++;
                } else {
                    losses++;
                }
            }
        }
        
        stats.put("wins", wins);
        stats.put("losses", losses);
        return stats;
    }
    
    public Map<String, Integer> getWinLossByPlayerAndType(Player player, String matchType) {
        Map<String, Integer> stats = new HashMap<>();
        int wins = 0;
        int losses = 0;
        
        for (Match match : matchRepository.getMatchesByType(matchType)) {
            List<Player> participants = match.getParticipants();
            boolean participated = participants.contains(player);
            
            if (participated) {
                if (match.getWinner()) {
                    wins++;
                } else {
                    losses++;
                }
            }
        }
        
        stats.put("wins", wins);
        stats.put("losses", losses);
        return stats;
    }
    
    public Map<String, Integer> getOverallRecord() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("wins", matchRepository.getWinCount());
        stats.put("losses", matchRepository.getLossCount());
        return stats;
    }
    
    public Map<String, Integer> getSeasonRecord(Season season) {
        Map<String, Integer> stats = new HashMap<>();
        int wins = 0;
        int losses = 0;
        
        for (Match match : matchRepository.getMatchesBySeason(season)) {
            if (match.getWinner()) {
                wins++;
            } else {
                losses++;
            }
        }
        
        stats.put("wins", wins);
        stats.put("losses", losses);
        return stats;
    }
    
    public double getWinPercentage(Player player) {
        Map<String, Integer> record = getWinLossByPlayer(player);
        int wins = record.get("wins");
        int losses = record.get("losses");
        int total = wins + losses;
        
        if (total == 0) {
            return 0.0;
        }
        
        return (wins * 100.0) / total;
    }
}

