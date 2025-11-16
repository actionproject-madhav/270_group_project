package com.rollins.tennis;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ExcelDataLoader {
    private static final String MENS_FILE = "rawDataMensTennis.xlsx";
    private static final String WOMENS_FILE = "rawDataWomensTennis.xlsx";
    
    public static class PlayerMatchData {
        public List<Player> players = new ArrayList<>();
        public List<Match> matches = new ArrayList<>();
    }
    
    public static PlayerMatchData loadData(String basePath) {
        PlayerMatchData data = new PlayerMatchData();
        Map<String, Player> playerMap = new HashMap<>();
        
        // Load men's data
        String mensPath = basePath + File.separator + MENS_FILE;
        if (new File(mensPath).exists()) {
            loadFromFile(mensPath, data, playerMap, "Men");
        }
        
        // Load women's data
        String womensPath = basePath + File.separator + WOMENS_FILE;
        if (new File(womensPath).exists()) {
            loadFromFile(womensPath, data, playerMap, "Women");
        }
        
        return data;
    }
    
    private static void loadFromFile(String filePath, PlayerMatchData data, 
                                    Map<String, Player> playerMap, String teamType) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Set<String> playerNames = new HashSet<>();
            
            // First pass: extract player names from sheet names
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                String sheetName = workbook.getSheetName(i);
                if (sheetName.endsWith("Singles") || sheetName.endsWith("Doubles")) {
                    String playerName = sheetName.replace("Singles", "").replace("Doubles", "");
                    playerNames.add(playerName);
                }
            }
            
            // Create Player objects from names
            int playerId = 1;
            for (String playerName : playerNames) {
                String[] nameParts = parsePlayerName(playerName);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                
                String playerIdStr = String.format("%s%03d", teamType.equals("Men") ? "M" : "W", playerId++);
                
                // Find matching image based on player name
                String imagePath = findPlayerImage(firstName, lastName);
                
                // Create player with default values (can be enhanced with actual data if available)
                Player player = new Player(playerIdStr, firstName, lastName, "2025", 
                                          inferNationality(lastName), 11.0, imagePath);
                
                if (!playerMap.containsKey(playerIdStr)) {
                    data.players.add(player);
                    playerMap.put(playerIdStr, player);
                }
            }
            
            // Second pass: load match data
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                
                if (sheetName.endsWith("Singles")) {
                    String playerName = sheetName.replace("Singles", "");
                    Player player = findPlayerByName(playerName, playerMap);
                    if (player != null) {
                        loadSinglesMatches(sheet, player, data);
                    }
                } else if (sheetName.endsWith("Doubles")) {
                    String playerName = sheetName.replace("Doubles", "");
                    Player player = findPlayerByName(playerName, playerMap);
                    if (player != null) {
                        loadDoublesMatches(sheet, player, playerMap, data);
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading Excel file: " + filePath);
            e.printStackTrace();
        }
    }
    
    private static String[] parsePlayerName(String name) {
        // Convert "LukeQuaynor" to ["Luke", "Quaynor"]
        // Convert "RichardStoiberer" to ["Richard", "Stoiberer"]
        // Handle camelCase names
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) && current.length() > 0) {
                parts.add(current.toString());
                current = new StringBuilder();
            }
            current.append(c);
        }
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        
        // If we couldn't split properly (e.g., all lowercase or no uppercase), 
        // try to split at common patterns
        if (parts.size() < 2 && name.length() > 5) {
            // Try to find a reasonable split point (after first 3-6 characters)
            int splitPoint = Math.min(6, name.length() / 2);
            return new String[]{
                name.substring(0, splitPoint),
                name.substring(splitPoint)
            };
        }
        
        return parts.toArray(new String[0]);
    }
    
    private static String inferNationality(String lastName) {
        // Simple inference based on common patterns (can be enhanced)
        if (lastName.isEmpty()) return "Unknown";
        // This is a placeholder - in a real system, you'd have a database or mapping
        return "Unknown";
    }
    
    private static String findPlayerImage(String firstName, String lastName) {
        // Map player names to image filenames in public/images folder
        // Images are named after the player's last name (or first name in some cases)
        String imageName = null;
        
        // Normalize names for matching
        String firstLower = firstName.toLowerCase();
        String lastLower = lastName.toLowerCase();
        
        // Match by last name first, then try first name
        if (lastLower.equals("anterist") || lastLower.equals("anterist")) {
            imageName = "Anterist.jpg";
        } else if (lastLower.equals("gusic") || firstLower.equals("fabian")) {
            // Try both gusic.webp and Fabian.jpg
            imageName = "Fabian.jpg";
        } else if (lastLower.equals("falster") || firstLower.equals("milla")) {
            imageName = "Falster.jpg";
        } else if (lastLower.equals("fruijtier") || firstLower.equals("stella")) {
            imageName = "Fruijtier.jpg";
        } else if (lastLower.equals("liu") || firstLower.equals("nancy")) {
            imageName = "Liu.jpg";
        } else if (lastLower.equals("mitrofanova") || firstLower.equals("nina")) {
            imageName = "Mitrofanova.jpg";
        } else if (lastLower.equals("cappelaro") || firstLower.equals("pietro")) {
            imageName = "Pietro.jpg";
        } else if (lastLower.equals("quaynor") || firstLower.equals("luke")) {
            imageName = "Quaynor.jpg";
        } else if (lastLower.equals("vlasova") || firstLower.equals("polina")) {
            imageName = "Vlasova.jpg";
        } else if (lastLower.equals("stoiberer") || firstLower.equals("richard") || firstLower.equals("richie")) {
            // Handle Richie - might be Richie.jpg or similar
            imageName = "Richie.jpg";
        }
        
        // Return image path if found, otherwise null
        if (imageName != null) {
            return "/images/" + imageName;
        }
        return null;
    }
    
    private static Player findPlayerByName(String playerName, Map<String, Player> playerMap) {
        String[] nameParts = parsePlayerName(playerName);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        for (Player player : playerMap.values()) {
            if (player.getFirstName().equals(firstName) && 
                player.getLastName().equals(lastName)) {
                return player;
            }
        }
        return null;
    }
    
    private static void loadSinglesMatches(Sheet sheet, Player player, PlayerMatchData data) {
        if (sheet.getPhysicalNumberOfRows() < 2) return; // Header + at least one row
        
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnMap = getColumnMap(headerRow);
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                LocalDate date = parseDateFromCell(row, columnMap.get("Date"));
                if (date == null) continue;
                
                String opponentTeam = getCellValue(row, columnMap.get("Opposing Team"));
                String opponentName = getCellValue(row, columnMap.get("Opponent Name"));
                String score = getCellValue(row, columnMap.get("Singles Score"));
                
                if (opponentTeam == null || opponentTeam.trim().isEmpty()) continue;
                if (score == null || score.trim().isEmpty()) continue;
                
                // Parse score to determine if Rollins won
                boolean rollinsWon = parseWinLoss(score);
                
                // Create opponent player (placeholder)
                Player opponentPlayer = new Player("OPP" + System.currentTimeMillis() + i, 
                                                   parseFirstName(opponentName), 
                                                   parseLastName(opponentName), 
                                                   "2024", "Unknown", 11.0);
                
                // Determine season from date
                Season season = determineSeason(date);
                
                // Create result
                Result result = new Result(score, rollinsWon);
                
                // Create match
                SinglesMatch match = new SinglesMatch(date, season, result, opponentTeam, 
                                                     player, opponentPlayer);
                data.matches.add(match);
                
            } catch (Exception e) {
                // Skip invalid rows
                continue;
            }
        }
    }
    
    private static void loadDoublesMatches(Sheet sheet, Player player, Map<String, Player> playerMap, 
                                          PlayerMatchData data) {
        if (sheet.getPhysicalNumberOfRows() < 2) return;
        
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnMap = getColumnMap(headerRow);
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                LocalDate date = parseDateFromCell(row, columnMap.get("Date"));
                if (date == null) continue;
                
                String opponentTeam = getCellValue(row, columnMap.get("Opposing Team"));
                String opponents = getCellValue(row, columnMap.get("Opponents"));
                String partnerName = getCellValue(row, columnMap.get("Partner"));
                String score = getCellValue(row, columnMap.get("Doubles Score"));
                
                if (opponentTeam == null || opponentTeam.trim().isEmpty()) continue;
                if (score == null || score.trim().isEmpty()) continue;
                
                // Find partner player
                Player partner = findPlayerByName(partnerName, playerMap);
                if (partner == null) continue;
                
                // Parse score
                boolean rollinsWon = parseWinLoss(score);
                
                // Parse opponent pair
                String[] opponentPair = parseOpponentPair(opponents);
                
                // Determine season
                Season season = determineSeason(date);
                
                // Create result
                Result result = new Result(score, rollinsWon);
                
                // Create match
                DoublesMatch match = new DoublesMatch(date, season, result, opponentTeam,
                                                      player, partner,
                                                      opponentPair[0], opponentPair[1]);
                data.matches.add(match);
                
            } catch (Exception e) {
                continue;
            }
        }
    }
    
    private static Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        if (headerRow == null) return map;
        
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String value = getCellValueAsString(cell);
                map.put(value, i);
            }
        }
        return map;
    }
    
    private static String getCellValue(Row row, Integer colIndex) {
        if (colIndex == null || row == null) return null;
        Cell cell = row.getCell(colIndex);
        return getCellValueAsString(cell);
    }
    
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    // Check if it's a whole number
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
    
    private static LocalDate parseDateFromCell(Row row, Integer colIndex) {
        if (colIndex == null || row == null) return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;
        
        try {
            // Check if cell is a date
            if (DateUtil.isCellDateFormatted(cell)) {
                java.util.Date date = cell.getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            
            // Try numeric (Excel serial date)
            if (cell.getCellType() == CellType.NUMERIC) {
                double numericValue = cell.getNumericCellValue();
                java.util.Date date = DateUtil.getJavaDate(numericValue);
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            
            // Try string parsing
            String dateStr = getCellValueAsString(cell);
            if (dateStr == null || dateStr.trim().isEmpty()) return null;
            
            // Try various date formats
            String[] formats = {
                "yyyy-MM-dd",
                "MM/dd/yy",
                "M/d/yy",
                "MM/dd/yyyy",
                "M/d/yyyy"
            };
            
            for (String format : formats) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
                    java.util.Date date = sdf.parse(dateStr.trim());
                    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            // Return null if parsing fails
        }
        
        return null;
    }
    
    private static boolean parseWinLoss(String score) {
        if (score == null) return false;
        String lower = score.toLowerCase().trim();
        return lower.startsWith("won");
    }
    
    private static String parseFirstName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "Unknown";
        String[] parts = fullName.split("\\s+|,");
        return parts.length > 0 ? parts[0].trim() : "Unknown";
    }
    
    private static String parseLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "Unknown";
        String[] parts = fullName.split("\\s+|,");
        return parts.length > 1 ? parts[parts.length - 1].trim() : "Unknown";
    }
    
    private static String[] parseOpponentPair(String opponents) {
        if (opponents == null) return new String[]{"Unknown", "Unknown"};
        String[] parts = opponents.split("/");
        if (parts.length == 2) {
            return new String[]{parts[0].trim(), parts[1].trim()};
        }
        return new String[]{opponents.trim(), "Unknown"};
    }
    
    private static Season determineSeason(LocalDate date) {
        int year = date.getYear();
        if (year >= 2024) return Season.YEAR_2024;
        if (year >= 2023) return Season.YEAR_2023;
        return Season.YEAR_2022;
    }
}

