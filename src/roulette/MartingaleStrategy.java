package roulette;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 7:05 PM
 *
 * BettingStrategy which implements the Martingale betting strategy, or a variation thereof.
 * Martingale strategy doubles the bet after each loss, to recuperate the previous loss.
 */
public class MartingaleStrategy implements BettingStrategy {
    /** The initial bet amount */
    private double startingBet;
    /** The maximum amount the player will bet (or max house bet) */
    private double maxBet;
    /** When "doubling" the bet to recoup prior loss, how much do we actually multiply by */
    private double multipler;
    /** How many losses to skip before the bet is multipled.  A delay of 0 means multiply by "multipler" after the first loss. */
    private long delay;
    /** The smallest denomination of the roulette chips, used to calculate the next bet. */
    private double denomination;
    /** If true, once max bet is reached, the maximum bet is bet until it "wins" */
    private Boolean dontGiveUp;

    public MartingaleStrategy(double startingBet, double maxBet, double multipler, long delay, double denomination, Boolean dontGiveUp) {
        this.startingBet = startingBet;
        this.maxBet = maxBet;
        this.multipler = multipler;
        this.delay = delay;
        this.denomination = denomination;
        this.dontGiveUp = dontGiveUp;
    }

    @Override
    public double getStartingBet() {
        return startingBet;
    }

    @Override
    public double getBetAmount(int iteration, double lastBetAmount, Number lastRolled) {
        if (iteration <= delay)
            return lastBetAmount;

        double nextBet = lastBetAmount * multipler;
        double credits = nextBet / denomination;

        nextBet = Math.ceil(credits) * denomination;

        if (nextBet > maxBet)
            return dontGiveUp ? maxBet : 0;

        return nextBet;
    }
}
