package com.rollins.tennis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchRepository {
    private List<Match> matches;
    private Map<Season, List<Match>> matchesBySeason;
    
    public MatchRepository() {
        this.matches = new ArrayList<>();
        this.matchesBySeason = new HashMap<>();
        for (Season season : Season.values()) {
            matchesBySeason.put(season, new ArrayList<>());
        }
    }
    
    public void addMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        
        matches.add(match);
        Season season = match.getSeason();
        matchesBySeason.get(season).add(match);
    }
    
    public List<Match> getAllMatches() {
        return new ArrayList<>(matches);
    }
    
    public List<Match> getMatchesBySeason(Season season) {
        if (season == null) {
            return getAllMatches();
        }
        return new ArrayList<>(matchesBySeason.get(season));
    }
    
    public List<Match> getMatchesByType(String matchType) {
        return matches.stream()
            .filter(m -> m.getMatchType().equals(matchType))
            .collect(Collectors.toList());
    }
    
    public List<Match> filterMatches(Season season, String matchType) {
        List<Match> results = matches;
        
        if (season != null) {
            results = getMatchesBySeason(season);
        }
        
        if (matchType != null && !matchType.trim().isEmpty()) {
            results = results.stream()
                .filter(m -> m.getMatchType().equals(matchType))
                .collect(Collectors.toList());
        }
        
        return results;
    }
    
    public List<Match> getMatchesByOpponent(String opponent) {
        if (opponent == null || opponent.trim().isEmpty()) {
            return getAllMatches();
        }
        
        String lowerOpponent = opponent.toLowerCase().trim();
        return matches.stream()
            .filter(m -> m.getOpponent().toLowerCase().contains(lowerOpponent))
            .collect(Collectors.toList());
    }
    
    public int getMatchCount() {
        return matches.size();
    }
    
    public int getWinCount() {
        return (int) matches.stream()
            .filter(Match::getWinner)
            .count();
    }
    
    public int getLossCount() {
        return matches.size() - getWinCount();
    }
}

