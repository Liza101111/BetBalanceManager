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
                Operation operation = Operation.valueOf(values[1].toUpperCase());
                UUID matchId = values.length > 2 && !values[2].isEmpty() ? UUID.fromString(values[2].trim()) : null;
                int coinNumber = Integer.parseInt(values[3]);
                String betSide = values.length > 4 ? values[4] : null;

                Player player = playerRegistry.getPlayer(playerId);

                switch (operation) {
                    case DEPOSIT:
                        player.deposit(coinNumber);
                        break;
                    case BET:
                        Match match = matchRegistry.getMatch(matchId);
                        player.placeBet(coinNumber, match, betSide);
                        break;
                    case WITHDRAW:
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

    public void writeIllegitimatePlayers(String outputFilePath, PlayerRegistry playerRegistry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            List<Player> illegitimatePlayers = playerRegistry.getIllegitimatePlayers();

            for (Player player : illegitimatePlayers) {
                List<Operation> illegalActions = player.getIllegalActions();

                if(!illegalActions.isEmpty()){
                    Operation firstIllegalAction = player.getIllegalActions().get(0);
                    UUID matchId = player.getMatchId() != null ? player.getMatchId() : null;
                    BetInfo betInfo = player.getBetsMap().get(matchId);

                    String matchIdStr = "null";
                    String betAmount = "null";
                    String betSide = "null";

                    if(firstIllegalAction == Operation.WITHDRAW){
                        int withdrawalAmount = player.getIllegalWithdrawalAmount();
                        betAmount = String.valueOf(withdrawalAmount);
                    }else{
                        matchIdStr = matchId != null ? matchId.toString() : "null";
                        betAmount = betInfo != null ? String.valueOf(betInfo.getBetAmount()) : "null";
                        betSide = betInfo != null ? betInfo.getBetSide(): "null";
                    }

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

    public void writeCasinoBalance(String outputFilePath, MatchRegistry matchRegistry, PlayerRegistry playerRegistry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true))) {
            BigDecimal casinoBalanceChange = calculateCasinoBalance(matchRegistry, playerRegistry);

            long casinoBalance = casinoBalanceChange.longValue();

            writer.newLine();
            writer.write(String.valueOf(casinoBalance));

        } catch (IOException e) {
            throw new RuntimeException("Error writing casino balance to file", e);
        }
    }

    private BigDecimal calculateCasinoBalance(MatchRegistry matchRegistry, PlayerRegistry playerRegistry) {
        BigDecimal totalAmountWon = BigDecimal.ZERO;
        BigDecimal totalAmountLost = BigDecimal.ZERO;

        for (Match match : matchRegistry.getMatches().values()) {
            for (Player player : playerRegistry.getLegitimatePlayers()) {
                BetInfo betInfo = player.getBetsMap().get(match.getMatchId());

                if (betInfo != null) {
                    if (match.getResult().toString().equals(betInfo.getBetSide()) || match.getResult().name().equals(betInfo.getBetSide())) {
                        totalAmountWon = totalAmountWon.add(
                                BigDecimal.valueOf(betInfo.getBetAmount()).multiply(match.getResultRate())
                        );
                    } else if(!match.getResult().equals(MatchResult.DRAW)){
                        totalAmountLost = totalAmountLost.add(BigDecimal.valueOf(betInfo.getBetAmount()));
                    }
                }
            }
        }

        BigDecimal netBalanceChange = totalAmountLost.subtract(totalAmountWon);
        System.out.println("Total Amount Won: " + totalAmountWon);
        System.out.println("Total Amount Lost: " + totalAmountLost);
        System.out.println("Net Balance Change: " + netBalanceChange);
        return netBalanceChange;
    }

    public void writeResults(String outputFilePath, PlayerRegistry playerRegistry, MatchRegistry matchRegistry) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writeLegitimatePlayers(outputFilePath, playerRegistry);

            writeIllegitimatePlayers(outputFilePath, playerRegistry);

            writeCasinoBalance(outputFilePath,matchRegistry,playerRegistry);
        } catch (IOException e) {
            throw new RuntimeException("Error writing results to file", e);
        }
    }

}
