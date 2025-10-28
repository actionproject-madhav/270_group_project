package com.rollins.tennis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SinglesMatch extends Match {
    private Player rollinsPlayer;
    private Player opponentPlayer;
    
    public SinglesMatch() {}
    
    public SinglesMatch(LocalDate date, Season season, Result result, String opponent,
                        Player rollinsPlayer, Player opponentPlayer) {
        super(date, season, result, opponent);
        this.rollinsPlayer = rollinsPlayer;
        this.opponentPlayer = opponentPlayer;
    }
    
    public Player getRollinsPlayer() { return rollinsPlayer; }
    public void setRollinsPlayer(Player rollinsPlayer) { this.rollinsPlayer = rollinsPlayer; }
    
    public Player getOpponentPlayer() { return opponentPlayer; }
    public void setOpponentPlayer(Player opponentPlayer) { this.opponentPlayer = opponentPlayer; }
    
    @Override
    public boolean getWinner() {
        return result.isRollinsWon();
    }
    
    @Override
    public List<Player> getParticipants() {
        List<Player> participants = new ArrayList<>();
        participants.add(rollinsPlayer);
        participants.add(opponentPlayer);
        return participants;
    }
    
    @Override
    public String getMatchType() {
        return "Singles";
    }
}

