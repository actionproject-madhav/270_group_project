package com.rollins.tennis;

public class Result {
    private String score;
    private boolean rollinsWon;
    
    public Result() {}
    
    public Result(String score, boolean rollinsWon) {
        this.score = score;
        this.rollinsWon = rollinsWon;
    }
    
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    
    public boolean isRollinsWon() { return rollinsWon; }
    public void setRollinsWon(boolean rollinsWon) { this.rollinsWon = rollinsWon; }
}

