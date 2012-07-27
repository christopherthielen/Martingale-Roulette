package roulette;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicStrategy implements Strategy {
    private double startingBet;
    private double maxBet;
    private double multipler;
    private long delay;
    private double denomination;
    private Boolean dontGiveUp;

    public BasicStrategy(double startingBet, double maxBet, double multipler, long delay, double denomination, Boolean dontGiveUp) {
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
