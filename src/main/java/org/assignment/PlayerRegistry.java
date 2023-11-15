package org.assignment;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerRegistry {
    private Map<UUID,Player> players = new HashMap<>();

    public Player getPlayer(UUID playerId){
        return players.computeIfAbsent(playerId, Player::new);
    }

    public List<Player> getLegitimatePlayers() {
        return players.values().stream()
                .filter(player -> !player.hasIllegalAction())
                .sorted(Comparator.comparing(player -> player.getPlayerId().toString()))
                .collect(Collectors.toList());
    }

    public List<Player> getIllegitimatePlayers() {
        return players.values().stream()
                .filter(Player::hasIllegalAction)
                .sorted(Comparator.comparing(player -> player.getPlayerId().toString()))
                .collect(Collectors.toList());
    }
}
