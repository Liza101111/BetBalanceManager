package org.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchRegistry {
    private Map<UUID, Match> matches = new HashMap<>();

    public Match getMatch(UUID matchId){
        if (matchId != null){
            return matches.get(matchId);
        }
        else {
            return null;
        }
    }

    public void addMatch(Match match){
        matches.put(match.getMatchId(), match);
    }

    public Map<UUID, Match> getMatches() {
        return matches;
    }
}
