package com.rollins.tennis;

public enum Season {
    YEAR_2022("2022"),
    YEAR_2023("2023"),
    YEAR_2024("2024");
    
    private final String year;
    
    Season(String year) {
        this.year = year;
    }
    
    public String getYear() {
        return year;
    }
    
    public static Season fromYear(String year) {
        for (Season season : values()) {
            if (season.year.equals(year)) {
                return season;
            }
        }
        throw new IllegalArgumentException("Invalid year: " + year);
    }
    
    @Override
    public String toString() {
        return year;
    }
}

