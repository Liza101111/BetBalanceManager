package org.assignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Player {
    private UUID playerId;
    private long balance;
    private int totalBets;
    private int totalWins;
    private Set<UUID> placedBets;

    public Player(UUID playerId) {
        this.playerId = playerId;
        this.balance = 0;
        this.totalBets = 0;
        this.totalWins = 0;
        this.placedBets = new HashSet<>();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getBalance() {
        return balance;
    }

    public int getTotalBets() {
        return totalBets;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public void placeBet(int betAmount, Match match, String betSide) {

        if(!placedBets.contains(match.getMatchId())) {
            placedBets.add(match.getMatchId());

            if (betAmount > 0 && betAmount <= balance) {
                balance -= betAmount;

                if(betSide.equals(MatchResult.A.name())){
                    // Player bet on the winning side
                    long winnings = BigDecimal.valueOf(betAmount * match.getRateA().doubleValue()).longValue();
                    balance += winnings;
                    totalWins++;
                    System.out.println("Bet placed on winning side A. Winnings: " + winnings);
                } else if(betSide.equals(MatchResult.B.name())){
                    long winnings = BigDecimal.valueOf(betAmount * match.getRateB().doubleValue()).longValue();
                    balance += winnings;
                    totalWins++;
                    System.out.println("Bet placed on winning side B. Winnings: " + winnings);
                } else if(betSide.equals(MatchResult.DRAW.name())){
                    balance += betAmount;
                    System.out.println("Match ended in a draw. Coins returned.");
                } else{
                    System.out.println("Bet placed on losing side. You lost the bet.");
                }

                totalBets++;
                System.out.println("New balance: " + balance);
            }else {
                System.out.println("Invalid bet amount or insufficient funds.");
            }
        }else{
            System.out.println("Player has already placed a bet on this match.");
        }
    }

    public BigDecimal getWinRate() {
        if (totalBets == 0) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(totalWins).divide(new BigDecimal(totalBets), 2, RoundingMode.HALF_UP);
        }
    }

}
