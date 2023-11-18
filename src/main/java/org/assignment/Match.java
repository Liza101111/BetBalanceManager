package org.assignment;

import java.math.BigDecimal;
import java.util.UUID;

public class Match {

    private UUID matchId;
    private BigDecimal rateA;
    private BigDecimal rateB;
    private MatchResult result;
    private int totalBets;

    public Match(UUID matchId, BigDecimal rateA, BigDecimal rateB, MatchResult result) {
        this.matchId = matchId;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
        this.totalBets = 0;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public BigDecimal getRateA() {
        return rateA;
    }

    public BigDecimal getRateB() {
        return rateB;
    }

    public MatchResult getResult() {
        return result;
    }

    public BigDecimal getResultRate() {
        return result == MatchResult.A ? rateA : result == MatchResult.B ? rateB : BigDecimal.ONE;
    }

    public int getTotalBets() {
        return totalBets;
    }

    public void incrementTotalBets() {
        totalBets++;
    }
}
