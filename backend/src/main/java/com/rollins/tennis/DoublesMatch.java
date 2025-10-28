package com.rollins.tennis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoublesMatch extends Match {
    private Player rollinsPlayer1;
    private Player rollinsPlayer2;
    private String opponentPair1;
    private String opponentPair2;
    
    public DoublesMatch() {}
    
    public DoublesMatch(LocalDate date, Season season, Result result, String opponent,
                        Player rollinsPlayer1, Player rollinsPlayer2,
                        String opponentPair1, String opponentPair2) {
        super(date, season, result, opponent);
        this.rollinsPlayer1 = rollinsPlayer1;
        this.rollinsPlayer2 = rollinsPlayer2;
        this.opponentPair1 = opponentPair1;
        this.opponentPair2 = opponentPair2;
    }
    
    public Player getRollinsPlayer1() { return rollinsPlayer1; }
    public void setRollinsPlayer1(Player rollinsPlayer1) { this.rollinsPlayer1 = rollinsPlayer1; }
    
    public Player getRollinsPlayer2() { return rollinsPlayer2; }
    public void setRollinsPlayer2(Player rollinsPlayer2) { this.rollinsPlayer2 = rollinsPlayer2; }
    
    public String getOpponentPair1() { return opponentPair1; }
    public void setOpponentPair1(String opponentPair1) { this.opponentPair1 = opponentPair1; }
    
    public String getOpponentPair2() { return opponentPair2; }
    public void setOpponentPair2(String opponentPair2) { this.opponentPair2 = opponentPair2; }
    
    @Override
    public boolean getWinner() {
        return result.isRollinsWon();
    }
    
    @Override
    public List<Player> getParticipants() {
        List<Player> participants = new ArrayList<>();
        participants.add(rollinsPlayer1);
        participants.add(rollinsPlayer2);
        return participants;
    }
    
    @Override
    public String getMatchType() {
        return "Doubles";
    }
}

