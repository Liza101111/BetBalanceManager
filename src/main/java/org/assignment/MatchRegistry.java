package org.assignment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchRegistry {
    private Map<UUID, Match> matches = new HashMap<>();

    public Match getMatch(UUID matchId){
        return matches.computeIfAbsent(matchId, Match::new);
    }
}
