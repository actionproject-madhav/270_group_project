package com.rollins.tennis;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class Server {
    private TennisController controller;
    private Gson gson;
    
    public Server() {
        this.controller = new TennisController();
        this.gson = new Gson();
    }
    
    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/players", new PlayersHandler());
        server.createContext("/api/matches", new MatchesHandler());
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/player/", new PlayerStatsHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080");
        System.out.println("API available at:");
        System.out.println("  GET /api/players");
        System.out.println("  GET /api/matches");
        System.out.println("  GET /api/stats");
        System.out.println("  GET /api/player/{id}");
    }
    
    private class PlayersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equals("GET")) {
                sendError(exchange, 405, "Method Not Allowed");
                return;
            }
            
            enableCORS(exchange);
            
            List<Player> players = controller.getAllPlayers();
            String response = gson.toJson(players);
            sendResponse(exchange, 200, response);
        }
    }
    
    private class MatchesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            
            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            
            String season = params.getOrDefault("season", "All");
            String type = params.getOrDefault("type", "All");
            String opponent = params.getOrDefault("opponent", null);
            
            List<TennisController.MatchData> matches = controller.getAllMatches(season, type, opponent);
            String response = gson.toJson(matches);
            sendResponse(exchange, 200, response);
        }
    }
    
    private class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            
            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String season = params.get("season");
            
            Map<String, Object> stats;
            if (season != null && !season.equals("All")) {
                stats = controller.getSeasonStats(season);
            } else {
                stats = controller.getOverallStats();
            }
            
            String response = gson.toJson(stats);
            sendResponse(exchange, 200, response);
        }
    }
    
    private class PlayerStatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            enableCORS(exchange);
            
            String path = exchange.getRequestURI().getPath();
            String playerId = path.substring(path.lastIndexOf('/') + 1);
            
            Map<String, Object> stats = controller.getPlayerStats(playerId);
            String response = gson.toJson(stats);
            sendResponse(exchange, 200, response);
        }
    }
    
    private void enableCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        
        if (exchange.getRequestMethod().equals("OPTIONS")) {
            try {
                exchange.sendResponseHeaders(200, -1);
            } catch (IOException e) {}
        }
    }
    
    private Map<String, String> parseQuery(String query) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
        if (query == null) return result;
        
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            }
        }
        return result;
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }
    
    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}

