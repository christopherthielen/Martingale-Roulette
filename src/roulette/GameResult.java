package roulette;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: sone
* Date: 7/7/12
* Time: 9:38 AM
*
* Data structure capturing the results of a "Game"
*/
public class GameResult {
    boolean win;
    /** Which NumberGroup the player was betting on */
    private final NumberGroup bet;
    /** Return on bet, when you win a spin */
    private final double odds;
    /** Number of iterations (spins) during this game */
    int iterations;
    /** The amount bet on the last spin of the game */
    double lastBet;
    /** The total amount bet during the game (sum of all bets, no winnings taken into account) */
    double totalBet;
    /** The total amount won or lost (sum of all winnings and all bets, where bets are negative numbers) */
    double totalWinnings;
    /** An ordered list of the spins that occurred during this "game" */
    private List<Number> spins;

    GameResult(boolean win, NumberGroup bet, double odds, int iterations, double lastBet, double totalBet, List<Number> spins) {
        this.win = win;
        this.bet = bet;
        this.odds = odds;
        this.iterations = iterations;
        this.lastBet = lastBet;
        this.totalBet = totalBet;
        this.spins = spins;
        this.totalWinnings = (lastBet * odds * (win ? 1.0 : 0.0)) - totalBet;
    }

    public String toString() {
        return ((win ? "Win!" : "Lose :(") + " Iterations: " + iterations + " last bet: " + lastBet + " total bet: " + totalBet + " Winnings: " + totalWinnings);
    }

    public List<Number> getSpins() {
        return spins;
    }
}
