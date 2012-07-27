package roulette;

/**
* Created with IntelliJ IDEA.
* User: sone
* Date: 7/7/12
* Time: 9:38 AM
* To change this template use File | Settings | File Templates.
*/
public class Results {
    boolean win;
    private final NumberGroups bet;
    private final double odds;
    int iterations;
    double lastBet;
    double totalBet;
    double totalWinnings;

    Results(boolean win, NumberGroups bet, double odds, int iterations, double lastBet, double totalBet) {
        this.win = win;
        this.bet = bet;
        this.odds = odds;
        this.iterations = iterations;
        this.lastBet = lastBet;
        this.totalBet = totalBet;
        this.totalWinnings = (lastBet * odds * (win ? 1.0 : 0.0)) - totalBet;
    }

    public String toString() {
        return ((win ? "Win!" : "Lose :(") + " Iterations: " + iterations + " last bet: " + lastBet + " total bet: " + totalBet + " Winnings: " + totalWinnings);
    }
}
