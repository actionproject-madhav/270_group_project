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
            java.util.Random random = new java.util.Random();
            for (String playerName : playerNames) {
                String[] nameParts = parsePlayerName(playerName);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                
                String playerIdStr = String.format("%s%03d", teamType.equals("Men") ? "M" : "W", playerId++);
                
                // Find matching image based on player name
                String imagePath = findPlayerImage(firstName, lastName);
                
                // Try to infer nationality from name, fallback to random
                String nationality = inferNationality(firstName, lastName);
                if (nationality.equals("Unknown")) {
                    nationality = getRandomNationality(random);
                }
                
                // Generate random class year (2024-2027 for current students)
                String classYear = generateRandomClassYear(random);
                
                // Create player with inferred/random values
                Player player = new Player(playerIdStr, firstName, lastName, classYear, 
                                          nationality, 11.0, imagePath);
                
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
    
    private static String inferNationality(String firstName, String lastName) {
        // Try to infer nationality based on name patterns
        String fullName = (firstName + " " + lastName).toLowerCase();
        String lastLower = lastName.toLowerCase();
        String firstLower = firstName.toLowerCase();
        
        // Known player nationalities based on common patterns
        if (lastLower.contains("stoiberer") || firstLower.contains("richard")) {
            return "Austria";
        } else if (lastLower.contains("anterist") || firstLower.contains("moritz")) {
            return "Germany";
        } else if (lastLower.contains("quaynor") || firstLower.contains("luke")) {
            return "United States";
        } else if (lastLower.contains("gusic") || firstLower.contains("fabian")) {
            return "Croatia";
        } else if (lastLower.contains("cappelaro") || firstLower.contains("pietro")) {
            return "Italy";
        } else if (lastLower.contains("fruijtier") || firstLower.contains("stella")) {
            return "Netherlands";
        } else if (lastLower.contains("falster") || firstLower.contains("milla")) {
            return "Denmark";
        } else if (lastLower.contains("vlasova") || firstLower.contains("polina")) {
            return "Russia";
        } else if (lastLower.contains("liu") || firstLower.contains("nancy")) {
            return "China";
        } else if (lastLower.contains("mitrofanova") || firstLower.contains("nina")) {
            return "Russia";
        }
        
        // Pattern-based inference for common name endings
        if (lastLower.endsWith("ski") || lastLower.endsWith("sky")) {
            return "Poland";
        } else if (lastLower.endsWith("ova") || lastLower.endsWith("ev") || lastLower.endsWith("ov")) {
            return "Russia";
        } else if (lastLower.endsWith("ic") || lastLower.endsWith("ich")) {
            return "Croatia";
        } else if (lastLower.endsWith("er") && lastLower.length() > 5) {
            return "Germany";
        } else if (lastLower.endsWith("son") || lastLower.endsWith("sen")) {
            return "Sweden";
        } else if (lastLower.endsWith("ez") || lastLower.endsWith("es")) {
            return "Spain";
        }
        
        return "Unknown";
    }
    
    private static String getRandomNationality(java.util.Random random) {
        String[] nationalities = {
            "United States", "Germany", "Spain", "France", "Italy", 
            "Netherlands", "Sweden", "Denmark", "Austria", "Switzerland",
            "Croatia", "Poland", "Russia", "China", "Japan", "Australia",
            "Canada", "Brazil", "Argentina", "United Kingdom"
        };
        return nationalities[random.nextInt(nationalities.length)];
    }
    
    private static String generateRandomClassYear(java.util.Random random) {
        // Generate class years between 2024-2027 (typical college years)
        int[] years = {2024, 2025, 2026, 2027};
        return String.valueOf(years[random.nextInt(years.length)]);
    }
    
    private static String findPlayerImage(String firstName, String lastName) {
        // Map player names to image filenames in public/images folder
        // Images are named after the player's last name (or first name in some cases)
        String imageName = null;
        
        // Normalize names for matching
        String firstLower = firstName.toLowerCase();
        String lastLower = lastName.toLowerCase();
        
        // Match by last name first, then try first name
        if (lastLower.equals("anterist")) {
            imageName = "Anterist.jpg";
        } else if (lastLower.equals("gusic") || firstLower.equals("fabian")) {
            // Prefer Fabian.jpg over gusic.webp
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
            // Try Richard.jpg first, then Richie.jpg as fallback
            imageName = "Richard.jpg";
        }
        
        // Return image path if found, otherwise null
        if (imageName != null) {
            return "/images/" + imageName;
        }
        return null;
    }
    
    private static Player findPlayerByName(String playerName, Map<String, Player> playerMap) {
        if (playerName == null || playerName.trim().isEmpty()) {
            return null;
        }
        
        // Try to parse the name (handles both "LukeQuaynor" and "Luke Quaynor")
        String[] nameParts = parsePlayerName(playerName.trim());
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // First try exact match
        for (Player player : playerMap.values()) {
            if (player.getFirstName().equalsIgnoreCase(firstName) && 
                player.getLastName().equalsIgnoreCase(lastName)) {
                return player;
            }
        }
        
        // If no exact match, try matching by full name (handles "Luke Quaynor" vs "LukeQuaynor")
        String normalizedSearch = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        for (Player player : playerMap.values()) {
            String normalizedPlayer = (player.getFirstName() + player.getLastName()).toLowerCase().replaceAll("\\s+", "");
            if (normalizedPlayer.equals(normalizedSearch)) {
                return player;
            }
        }
        
        // Last resort: try partial match on last name
        if (!lastName.isEmpty()) {
            for (Player player : playerMap.values()) {
                if (player.getLastName().equalsIgnoreCase(lastName)) {
                    return player;
                }
            }
        }
        
        return null;
    }
    
    private static void loadSinglesMatches(Sheet sheet, Player player, PlayerMatchData data) {
        if (sheet.getPhysicalNumberOfRows() < 2) return; // Header + at least one row
        
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnMap = getColumnMap(headerRow);
        
        int matchesLoaded = 0;
        int matchesSkipped = 0;
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                // Skip if this row looks like a header row (check if first cell contains "Date")
                String firstCell = getCellValue(row, 0);
                if (firstCell != null && firstCell.equalsIgnoreCase("Date")) {
                    continue; // Skip duplicate header rows
                }
                
                LocalDate date = parseDateFromCell(row, columnMap.get("Date"));
                if (date == null) {
                    matchesSkipped++;
                    continue;
                }
                
                String opponentTeam = getCellValue(row, columnMap.get("Opposing Team"));
                if (opponentTeam == null) {
                    opponentTeam = getCellValue(row, columnMap.get("opposing team"));
                }
                
                String opponentName = getCellValue(row, columnMap.get("Opponent Name"));
                if (opponentName == null) {
                    opponentName = getCellValue(row, columnMap.get("opponent name"));
                }
                
                String score = getCellValue(row, columnMap.get("Singles Score"));
                if (score == null) {
                    score = getCellValue(row, columnMap.get("singles score"));
                }
                
                // Clean opponent team name (remove special characters)
                if (opponentTeam != null) {
                    opponentTeam = opponentTeam.replaceAll("[\\u00A0\\u2007\\u202F]", " ").trim();
                    // Remove ranking info like "(#4)" but keep the team name
                    opponentTeam = opponentTeam.replaceAll("\\(#\\d+\\)", "").trim();
                }
                
                if (opponentTeam == null || opponentTeam.trim().isEmpty()) {
                    matchesSkipped++;
                    continue;
                }
                if (score == null || score.trim().isEmpty()) {
                    matchesSkipped++;
                    continue;
                }
                
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
                matchesLoaded++;
                
            } catch (Exception e) {
                matchesSkipped++;
                // Log error for debugging but continue
                System.err.println("Error loading singles match row " + i + " for " + player.getFullName() + ": " + e.getMessage());
                continue;
            }
        }
        
        System.out.println("Loaded " + matchesLoaded + " singles matches for " + player.getFullName() + " (skipped " + matchesSkipped + " rows)");
    }
    
    private static void loadDoublesMatches(Sheet sheet, Player player, Map<String, Player> playerMap, 
                                          PlayerMatchData data) {
        if (sheet.getPhysicalNumberOfRows() < 2) return;
        
        Row headerRow = sheet.getRow(0);
        Map<String, Integer> columnMap = getColumnMap(headerRow);
        
        // Handle typo in header: "Postion" instead of "Position"
        if (!columnMap.containsKey("Opponents") && columnMap.containsKey("Postion")) {
            // Try alternative column names
        }
        
        int matchesLoaded = 0;
        int matchesSkipped = 0;
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            try {
                // Skip if this row looks like a header row
                String firstCell = getCellValue(row, 0);
                if (firstCell != null && firstCell.equalsIgnoreCase("Date")) {
                    continue; // Skip duplicate header rows
                }
                
                LocalDate date = parseDateFromCell(row, columnMap.get("Date"));
                if (date == null) {
                    matchesSkipped++;
                    continue;
                }
                
                String opponentTeam = getCellValue(row, columnMap.get("Opposing Team"));
                if (opponentTeam == null) {
                    opponentTeam = getCellValue(row, columnMap.get("opposing team"));
                }
                
                String opponents = getCellValue(row, columnMap.get("Opponents"));
                if (opponents == null) {
                    opponents = getCellValue(row, columnMap.get("opponents"));
                }
                
                String partnerName = getCellValue(row, columnMap.get("Partner"));
                if (partnerName == null) {
                    partnerName = getCellValue(row, columnMap.get("partner"));
                }
                
                String score = getCellValue(row, columnMap.get("Doubles Score"));
                if (score == null) {
                    score = getCellValue(row, columnMap.get("doubles score"));
                }
                
                // Clean opponent team name
                if (opponentTeam != null) {
                    opponentTeam = opponentTeam.replaceAll("[\\u00A0\\u2007\\u202F]", " ").trim();
                    opponentTeam = opponentTeam.replaceAll("\\(#\\d+\\)", "").trim();
                }
                
                if (opponentTeam == null || opponentTeam.trim().isEmpty()) {
                    matchesSkipped++;
                    continue;
                }
                if (score == null || score.trim().isEmpty()) {
                    matchesSkipped++;
                    continue;
                }
                
                // Find partner player
                Player partner = null;
                if (partnerName != null && !partnerName.trim().isEmpty()) {
                    partner = findPlayerByName(partnerName.trim(), playerMap);
                    if (partner == null) {
                        matchesSkipped++;
                        System.err.println("Could not find partner: '" + partnerName + "' for " + player.getFullName());
                        continue;
                    }
                } else {
                    matchesSkipped++;
                    System.err.println("Partner name is empty for " + player.getFullName());
                    continue;
                }
                
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
                matchesLoaded++;
                
            } catch (Exception e) {
                matchesSkipped++;
                System.err.println("Error loading doubles match row " + i + " for " + player.getFullName() + ": " + e.getMessage());
                continue;
            }
        }
        
        System.out.println("Loaded " + matchesLoaded + " doubles matches for " + player.getFullName() + " (skipped " + matchesSkipped + " rows)");
    }
    
    private static Map<String, Integer> getColumnMap(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        if (headerRow == null) return map;
        
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    // Store both exact and case-insensitive versions
                    map.put(value, i);
                    map.put(value.toLowerCase(), i);
                    
                    // Handle common typos
                    if (value.equalsIgnoreCase("Postion")) {
                        map.put("Position", i);
                        map.put("position", i);
                    }
                }
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
                "M/d/yyyy",
                "M/dd/yy",
                "MM/d/yy"
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
        
        // Check for explicit win indicators
        if (lower.startsWith("won")) {
            return true;
        }
        
        // Check for loss indicators
        if (lower.startsWith("lost") || lower.startsWith("loss")) {
            return false;
        }
        
        // If score contains "unfinished" or "retired", we can't determine win/loss from score alone
        // Default to false (loss) for unfinished matches unless explicitly marked as won
        if (lower.contains("unfinished") || lower.contains("retired")) {
            return false;
        }
        
        // For scores like "6-1, 6-1" or "6-0, 6-3", we need to check if it starts with "Won"
        // If no explicit indicator, default to false
        return false;
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
        // Handle future dates (2025+) by mapping to 2024 season
        // Or create a new season if needed - for now, map 2025+ to 2024
        if (year >= 2025) return Season.YEAR_2024;
        if (year >= 2024) return Season.YEAR_2024;
        if (year >= 2023) return Season.YEAR_2023;
        if (year >= 2022) return Season.YEAR_2022;
        // For dates before 2022, default to 2022
        return Season.YEAR_2022;
    }
}

