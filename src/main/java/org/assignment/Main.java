package org.assignment;

public class Main {
    public static void main(String[] args) {

        PlayerRegistry playerRegistry = new PlayerRegistry();
        MatchRegistry matchRegistry = new MatchRegistry();

        BettingDataProcessor bettingDataProcessor = new BettingDataProcessor();
        processPlayerData("C:\\Users\\lixia\\IdeaProjects\\BetBalanceManager\\src\\main\\resources\\player_data.txt",bettingDataProcessor, playerRegistry, matchRegistry);
        processMatchData("C:\\Users\\lixia\\IdeaProjects\\BetBalanceManager\\src\\main\\resources\\match_data.txt", bettingDataProcessor, matchRegistry);

    }
    private static void processPlayerData(String filePath, BettingDataProcessor bettingDataProcessor, PlayerRegistry playerRegistry, MatchRegistry matchRegistry) {
        bettingDataProcessor.processPlayerData(filePath, playerRegistry, matchRegistry);
    }

    private static void processMatchData(String filePath, BettingDataProcessor bettingDataProcessor, MatchRegistry matchRegistry){
        bettingDataProcessor.processMatchData(filePath, matchRegistry);
    }

}