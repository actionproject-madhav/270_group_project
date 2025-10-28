package com.rollins.tennis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RosterManager {
    private List<Player> players;
    private Map<String, Player> playerMap;
    
    public RosterManager() {
        this.players = new ArrayList<>();
        this.playerMap = new HashMap<>();
    }
    
    public void addPlayer(Player player) {
        if (player == null || player.getId() == null) {
            throw new IllegalArgumentException("Player or player ID cannot be null");
        }
        
        if (playerMap.containsKey(player.getId())) {
            throw new IllegalArgumentException("Player with ID " + player.getId() + " already exists");
        }
        
        players.add(player);
        playerMap.put(player.getId(), player);
    }
    
    public Player findPlayerById(String id) {
        return playerMap.get(id);
    }
    
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }
    
    public List<Player> searchPlayers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPlayers();
        }
        
        query = query.toLowerCase().trim();
        List<Player> results = new ArrayList<>();
        
        for (Player player : players) {
            String fullName = player.getFullName().toLowerCase();
            if (fullName.contains(query)) {
                results.add(player);
            }
        }
        
        return results;
    }
}

