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
    private List<Operation> illegalActions = new ArrayList<>();

    private Map<UUID, BetInfo> betsMap = new HashMap<>();
    private static final String SIDE_A = "A";
    private static final String SIDE_B = "B";
    private int illegalWithdrawalAmount;

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

    public List<Operation> getIllegalActions() {
        return illegalActions;
    }

    public Map<UUID, BetInfo> getBetsMap() {
        return betsMap;
    }

    public long deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New balance: " + balance);
        } else {
            illegalActions.add(Operation.DEPOSIT);
            System.out.println("Invalid deposit amount.");
        }
        return balance;
    }

    public long withdraw(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            illegalActions.add(Operation.WITHDRAW);
            illegalWithdrawalAmount = amount;
            System.out.println("Invalid withdrawal amount or insufficient funds: " + amount);
        }
        return balance;
    }

    public int getIllegalWithdrawalAmount() {
        return illegalWithdrawalAmount;
    }

    public void placeBet(int betAmount, Match match, String betSide) {

        if(!placedBets.contains(match.getMatchId())) {
            placedBets.add(match.getMatchId());

            if(betAmount > 0 && betAmount <= balance){

                if(isValidBetSide(betSide)){
                    boolean winFlag = (betSide.equals(match.getResult().toString()));
                    boolean drawFlag = match.getResult().equals(MatchResult.DRAW);
                    if(winFlag || drawFlag){
                        switch (match.getResult()) {
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
                        betsMap.put(match.getMatchId(), new BetInfo(betAmount, betSide));
                        System.out.println("New balance: " + balance);
                    }else {
                        totalBets++;
                        betsMap.put(match.getMatchId(), new BetInfo(betAmount, betSide));
                        balance -= betAmount;
                        System.out.println("Bet placed on losing side: " + "Losing: "+betAmount);
                        System.out.println("New balance: " + balance);
                    }
                }else {
                    illegalActions.add(Operation.BET);
                    System.out.println("Invalid bet side.");
                }
            }else {
                illegalActions.add(Operation.BET);
                System.out.println("Invalid bet amount or insufficient funds.");
            }
        }else{
            illegalActions.add(Operation.BET);
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
        System.out.println("winning amount:"+winnings);
        balance += winnings;
        totalWins++;
        System.out.println("Bet placed on winning side " + side + ". Winnings: " + winnings);
    }

    private void handleDrawBet(long betAmount) {
        System.out.println("Match ended in a draw. Coins returned:" + betAmount);
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

}
