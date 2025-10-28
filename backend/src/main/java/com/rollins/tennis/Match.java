package com.rollins.tennis;

import java.time.LocalDate;
import java.util.List;

public abstract class Match {
    protected LocalDate date;
    protected Season season;
    protected Result result;
    protected String opponent;
    
    public Match() {}
    
    public Match(LocalDate date, Season season, Result result, String opponent) {
        this.date = date;
        this.season = season;
        this.result = result;
        this.opponent = opponent;
    }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public Season getSeason() { return season; }
    public void setSeason(Season season) { this.season = season; }
    
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }
    
    public String getOpponent() { return opponent; }
    public void setOpponent(String opponent) { this.opponent = opponent; }
    
    public abstract boolean getWinner();
    public abstract List<Player> getParticipants();
    public abstract String getMatchType();
}

