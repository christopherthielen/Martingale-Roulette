package roulette;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Strategy {
    double getStartingBet();
    double getBetAmount(int iteration, double lastBetAmount, Number lastRolled);
}
