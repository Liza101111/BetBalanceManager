package org.assignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class Player {
    private UUID playerId;
    private long balance;
    private int totalBets;
    private int totalWins;

    public Player(UUID playerId) {
        this.playerId = playerId;
        this.balance = 0;
        this.totalBets = 0;
        this.totalWins = 0;
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

    public void placeBet(int betAmount, Match match) {
        if (betAmount > 0 && betAmount <= balance) {
            balance -= betAmount;
            String betSide = match.getResult();

            if("A".equals(betSide)){
                // Player bet on the winning side
                long winnings = (long) (betAmount * match.getRateA().doubleValue());
                balance += winnings;
                System.out.println("Bet placed on winning side. Winnings: " + winnings);
            } else if("B".equals(betSide)){
                System.out.println("Bet placed on losing side. You lost the bet.");
            } else{
                balance += betAmount;
                System.out.println("Match ended in a draw. Coins returned.");
            }
        } else {
            System.out.println("Invalid bet amount or insufficient funds.");
        }
    }

}
