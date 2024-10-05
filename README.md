# Betting Data Processing Program

## Introduction
This Java program processes betting data, including player operations and match results. It simulates player actions such as placing bets, deposits, and withdrawals, while calculating final balances, win rates, and changes to the casino’s balance. The program also handles illegal actions and outputs relevant results in a structured format.

## Description
The program processes data for multiple players and matches. Key aspects include:
- **Player operations**: Bet, Deposit, Withdraw.
- **Match results**: 
  - **A**: Side A won.
  - **B**: Side B won.
  - **Draw**: Match ended in a draw.
- **Betting results**: Players gain or lose coins based on match outcomes and bet sizes, calculated using the corresponding side's rate.
- **Illegal actions**: Occur when a player attempts to bet or withdraw more coins than they possess.

### Key Operations
- **Betting**: Players can only bet on one side of a match.
- **Coins**: Calculations are based on integer values, and all players start with a balance of 0 coins.
- **Illegal Actions**: Illegal operations are flagged but do not affect the casino’s balance.

## Input Files
Two input files are provided in the `resources` folder:
- **`player_data.txt`**: Contains details about player actions (deposit, bet, withdraw).
- **`match_data.txt`**: Contains match information, including the result and rates for sides A and B.


## Output
The program generates a results file named `results.txt` in the project directory. The output is divided into three sections:

1. **Legitimate Players**: Players who did not perform any illegal actions, along with their final balance and betting win rate.
   - **Example**:  
   `163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4321 0.80`

2. **Illegitimate Players**: Players who attempted illegal actions, along with details of their first illegal operation.
   - **Example**:  
   `163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 5000 A`

3. **Casino Balance**: Net change in the casino’s balance based on player betting outcomes.
   - **Example**:  
   `1500`

**Note**: If any section has no data, an empty line will be written for that section.

## Assignment Rules

- **Input File Format**:
  - **`player_data.txt`**: Contains player actions with unique player IDs.
  - **`match_data.txt`**: Contains match results and rates for sides A and B.

- **Calculation**:
  - Players gain coins when betting on the winning side, lose coins on the losing side, and get their coins returned in case of a draw.
  - Illegal operations are ignored for casino balance calculation.

- **Rounding**: All coin calculations are rounded down to integer values.

## Running the Program

1. Place `player_data.txt` and `match_data.txt` in the `resources` folder.
2. Compile and run the Java program.
3. The `results.txt` file will be generated in the project directory next to the `Main` class.

### Example Output:
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4321 0.80

163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 5000 A

1500
