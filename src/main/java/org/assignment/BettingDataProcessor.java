package org.assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BettingDataProcessor {

    public void processPlayerData(String playerDataFilePath, PlayerRegistry playerRegistry, MatchRegistry matchRegistry){
        try(BufferedReader br = new BufferedReader(new FileReader(playerDataFilePath))) {
            String line;

            while ((line = br.readLine()) != null){
                String[] values = line.split(",");

                UUID playerId = UUID.fromString(values[0]);
                String operation = values[1];
                UUID matchId = values.length > 2 && !values[2].isEmpty() ? UUID.fromString(values[2].trim()) : null;
                int coinNumber = Integer.parseInt(values[3]);
                String betSide = values.length > 4 ? values[4] : null;

                Player player = playerRegistry.getPlayer(playerId);
                Match match = matchRegistry.getMatch(matchId);

                switch (operation) {
                    case "DEPOSIT":
                        player.deposit(coinNumber);
                        break;
                    case "BET" :
                        player.placeBet(coinNumber, match, betSide);
                        break;
                    case "WITHDRAW":
                        player.withdraw(coinNumber);
                        break;
                    default:
                        System.out.println("Invalid operation: " + operation);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
