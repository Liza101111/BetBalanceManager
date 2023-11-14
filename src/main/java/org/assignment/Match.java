package org.assignment;

import java.math.BigDecimal;
import java.util.UUID;

public class Match {

    private UUID matchId;
    private BigDecimal rateA;
    private BigDecimal rateB;
    private MatchResult result;

    public Match(UUID matchId, BigDecimal rateA, BigDecimal rateB, MatchResult result) {
        this.matchId = matchId;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
    }
    public Match(UUID matchId) {
        this(matchId, BigDecimal.ONE, BigDecimal.ONE, MatchResult.DRAW);
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
}
