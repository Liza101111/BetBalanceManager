package org.assignment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Player {
    private UUID playerId;
    private long balance;
    private int totalBets;
    private int totalWins;
    private Set<UUID> placedBets;
    private List<IllegalAction> illegalActions = new ArrayList<>();

    private Map<UUID, Integer> betAmountsMap = new HashMap<>();
    private Map<UUID, String> betSidesMap = new HashMap<>();

    private static final String SIDE_A = "A";
    private static final String SIDE_B = "B";

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

    public List<IllegalAction> getIllegalActions() {
        return illegalActions;
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
            illegalActions.add(IllegalAction.INSUFFICIENT_BALANCE);
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public void placeBet(int betAmount, Match match, String betSide) {

        if(!placedBets.contains(match.getMatchId())) {
            placedBets.add(match.getMatchId());

            if(betAmount > 0 && betAmount <= balance){
                balance -= betAmount;

                if(isValidBetSide(betSide)){
                    switch (match.getResult()){
                        case A:
                            handleWinningBet(match.getRateA(), betAmount, SIDE_A);
                            break;
                        case B:
                            handleWinningBet(match.getRateB(), betAmount, SIDE_B);
                            break;
                        case DRAW:
                            handleDrawBet(betAmount);
                            break;
                        default:
                            System.out.println("Unexpected match result. Unable to process the bet.");
                    }

                    totalBets++;
                    betAmountsMap.put(match.getMatchId(), betAmount);
                    betSidesMap.put(match.getMatchId(), betSide);
                    System.out.println("New balance: " + balance);
                }else {
                    illegalActions.add(IllegalAction.INVALID_BET_SIDE);
                    System.out.println("Invalid bet side.");
                }
            }else {
                illegalActions.add(IllegalAction.INSUFFICIENT_BALANCE);
                System.out.println("Invalid bet amount or insufficient funds.");
            }
        }else{
            illegalActions.add(IllegalAction.MULTIPLE_BETS_ON_SAME_MATCH);
            System.out.println("Player has already placed a bet on this match.");
        }
    }

    public boolean hasIllegalAction() {
        return !illegalActions.isEmpty();
    }

    private boolean isValidBetSide(String betSide) {
        return betSide.equals(SIDE_A) || betSide.equals(SIDE_B);
    }

    private void handleWinningBet(BigDecimal rate, long betAmount, String side) {
        long winnings = BigDecimal.valueOf(betAmount * rate.doubleValue()).longValue();
        balance += winnings;
        totalWins++;
        System.out.println("Bet placed on winning side " + side + ". Winnings: " + winnings);
    }

    private void handleDrawBet(long betAmount) {
        balance += betAmount;
        System.out.println("Match ended in a draw. Coins returned.");
    }

    public BigDecimal getWinRate() {
        if (totalBets == 0) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(totalWins).divide(new BigDecimal(totalBets), 2, RoundingMode.HALF_UP);
        }
    }

    public UUID getMatchId() {
        if (!placedBets.isEmpty()) {
            return placedBets.iterator().next();
        } else {
            return null;
        }
    }

    public Integer getBetAmount() {

        if (!placedBets.isEmpty()) {
            UUID firstBetId = placedBets.iterator().next();
            return betAmountsMap.get(firstBetId);
        } else {
            return null;
        }
    }

    public String getBetSide() {
        if (!placedBets.isEmpty()) {
            UUID firstBetId = placedBets.iterator().next();
            return betSidesMap.get(firstBetId);
        } else {
            return null; // No betSide found
        }
    }
}
