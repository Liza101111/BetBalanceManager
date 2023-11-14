package org.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchRegistry {
    private Map<UUID, Match> matches = new HashMap<>();

    public Match getMatch(UUID matchId){
        return matches.computeIfAbsent(matchId, Match::new);
    }

    public void addMatch(Match match){
        matches.put(match.getMatchId(), match);
    }
}
