package org.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRegistry {
    private Map<UUID,Player> players = new HashMap<>();

    public Player getPlayer(UUID playerId){
        return players.computeIfAbsent(playerId, Player::new);
    }
}
