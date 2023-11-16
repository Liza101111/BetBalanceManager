package org.assignment;

public class BetInfo {

    private final int betAmount;
    private final String betSide;

    public int getBetAmount() {
        return betAmount;
    }

    public String getBetSide() {
        return betSide;
    }

    public BetInfo(int betAmount, String betSide) {
        this.betAmount = betAmount;
        this.betSide = betSide;
    }
}
