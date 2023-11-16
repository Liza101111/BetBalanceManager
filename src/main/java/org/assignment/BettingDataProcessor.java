package org.assignment;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class BettingDataProcessor {

    private Operation operation;

    private long casinoBalance = 0;

    public void processPlayerData(String playerDataFilePath, PlayerRegistry playerRegistry, MatchRegistry matchRegistry){
        try(BufferedReader br = new BufferedReader(new FileReader(playerDataFilePath))) {
            String line;

            while ((line = br.readLine()) != null){
                String[] values = line.split(",");

                UUID playerId = UUID.fromString(values[0]);
                String operationStr = values[1];
                UUID matchId = values.length > 2 && !values[2].isEmpty() ? UUID.fromString(values[2].trim()) : null;
                int coinNumber = Integer.parseInt(values[3]);
                String betSide = values.length > 4 ? values[4] : null;

                Player player = playerRegistry.getPlayer(playerId);
                Match match = matchRegistry.getMatch(matchId);

                try {
                    Operation operation = Operation.valueOf(operationStr.toUpperCase());
                    processPlayerOperation(player, match, operation, coinNumber, betSide);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid operation: " + operationStr);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processPlayerOperation(Player player, Match match, Operation operation, int coinNumber, String betSide) {
        switch (operation) {
            case DEPOSIT:
                player.deposit(coinNumber);
                break;
            case BET:
                player.placeBet(coinNumber, match, betSide);
                break;
            case WITHDRAW:
                player.withdraw(coinNumber);
                break;
            default:
                System.out.println("Invalid operation: " + operation);
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

    public void writeIllegitimatePlayers(String outputFilePath, PlayerRegistry playerRegistry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            List<Player> illegitimatePlayers = playerRegistry.getIllegitimatePlayers();

            for (Player player : illegitimatePlayers) {
                List<Operation> illegalActions = player.getIllegalActions();

                if(!illegalActions.isEmpty()){
                    Operation firstIllegalAction = player.getIllegalActions().get(0);

                    UUID matchId = player.getMatchId() != null ? player.getMatchId() : null;
                    BetInfo betInfo = player.getBetsMap().get(matchId);

                    String matchIdStr = matchId != null ? matchId.toString() : "null";
                    String betAmount = betInfo != null ? String.valueOf(betInfo.getBetAmount()) : "null";
                    String betSide = betInfo != null ? betInfo.getBetSide(): "null";

                    String outputLine = String.format("%s %s %s %s %s",
                            player.getPlayerId(), firstIllegalAction, matchIdStr, betAmount, betSide);

                    writer.newLine();
                    writer.write(outputLine);
                    writer.newLine();
                }else {
                    System.out.println("Player has no illegal actions: " + player.getPlayerId());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing illegitimate players to file", e);
        }
    }

    private long calculateMatchResultBalance(Match match) {
        long balanceChange = 0;

        switch (match.getResult()) {
            case A:
                balanceChange = match.getRateA().multiply(BigDecimal.valueOf(match.getTotalBets())).longValue();
            case B:
                balanceChange = match.getRateB().multiply(BigDecimal.valueOf(match.getTotalBets())).longValue();
            case DRAW:
                break;
            default:
                throw new IllegalArgumentException("Unexpected match result: " + match.getResult());
        }

        return balanceChange;
    }

    private long calculateCasinoBalance(Map<UUID, Match> matches) {
        long totalBalanceChange = 0;

        for (Match match : matches.values()) {
            totalBalanceChange += calculateMatchResultBalance(match);
        }

        return totalBalanceChange;
    }

    public void writeCasinoBalance(String outputFilePath, MatchRegistry matchRegistry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {

            Map<UUID, Match> matches = matchRegistry.getMatches();
            long casinoBalanceChange = calculateCasinoBalance(matches);

            writer.newLine();
            writer.write(String.valueOf (casinoBalance + casinoBalanceChange));

        } catch (IOException e) {
            throw new RuntimeException("Error writing casino balance to file", e);
        }
    }

    public void writeResults(String outputFilePath, PlayerRegistry playerRegistry, MatchRegistry matchRegistry) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writeLegitimatePlayers(outputFilePath, playerRegistry);

            writeIllegitimatePlayers(outputFilePath, playerRegistry);

            writeCasinoBalance(outputFilePath,matchRegistry);
        } catch (IOException e) {
            throw new RuntimeException("Error writing results to file", e);
        }
    }

}
