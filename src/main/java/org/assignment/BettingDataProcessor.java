package org.assignment;

import java.io.*;
import java.math.BigDecimal;
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

    public void processMatchData(String matchDataFilePath, MatchRegistry matchRegistry){
        try(BufferedReader br = new BufferedReader(new FileReader(matchDataFilePath))){
            String line;

            while ((line = br.readLine()) != null){
                String[] values = line.split(",");

                UUID matchId = UUID.fromString(values[0]);
                BigDecimal rateA = new BigDecimal(values[1]);
                BigDecimal rateB = new BigDecimal(values[2]);
                MatchResult result = MatchResult.valueOf(values[3]);

                Match match = new Match(matchId, rateA, rateB, result);
                matchRegistry.addMatch(match);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeLegitimatePlayers(String outputPath, PlayerRegistry playerRegistry) {

        List<Player> legitimatePlayers = playerRegistry.getLegitimatePlayers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (Player player : legitimatePlayers) {
                BigDecimal winRate = player.getWinRate();
                String outputLine = String.format("%s %d %.2f", player.getPlayerId(), player.getBalance(), winRate);
                writer.write(outputLine);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing legitimate players to file", e);
        }
    }


}
