package roulette;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 7:04 PM
 *
 * Defines the betting strategy
 */
public interface BettingStrategy {
    /** @return the Initial bet */
    double getStartingBet();
    /**
     * @param iteration the iteration (spin) number for this "game"
     * @param lastBetAmount the amount bet on the previous iteration
     * @param lastRolled the number spun last time
     * @return the next bet amount
     */
    double getBetAmount(int iteration, double lastBetAmount, Number lastRolled);
}
