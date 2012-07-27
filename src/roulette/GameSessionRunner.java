package roulette;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/27/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameSessionRunner implements Runnable {
    static SecureRandom secureRandom = new SecureRandom();

    private Roulette gui;
    private BettingStrategy bettingStrategy;
    private long iterations;
    private WheelType wheelType;
    private NumberGroup bet;

    public GameSessionRunner(Roulette gui, BettingStrategy strategy, long iterations, WheelType wheelType, NumberGroup bet) {
        this.wheelType = wheelType;
        this.bet = bet;
        this.gui = gui;
        this.bettingStrategy = strategy;
        this.iterations = iterations;
    }

    public void run() {
        long sessionSpins = 0;
        double sessionBets = 0;
        double sessionWinLoss = 0;
        double runningWinLossD = 0;
        for (int i = 0; i < iterations; i++) {
            GameResult gameResult = runGame();

            gui.recordResult(sessionSpins, sessionBets, sessionWinLoss, runningWinLossD, gameResult);

            runningWinLossD += gameResult.totalWinnings;
            sessionSpins += gameResult.iterations;
            sessionBets += gameResult.totalBet;
            sessionWinLoss += gameResult.totalWinnings;

            try {
                Thread.sleep(Math.min(100, 4000 / iterations));
            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            }
        }
    }


    public static int spinWheel(WheelType wheelType) {
        int mult = 0;
        switch (wheelType) {
            case OneZero:
                mult = 37;
                break;
            case TwoZeros:
                mult = 38;
                break;
        }
        return (int) (secureRandom.nextDouble() * mult + 1);
    }


    private GameResult runGame() {
        double totalBet = 0.0;
        double betAmount = bettingStrategy.getStartingBet();
        double lastbet = 0;

        int i = 0;
        Number spin;
        List<Number> spins = new ArrayList<Number>();
        while (true) {
            lastbet = betAmount;
            totalBet += betAmount;
            i++;
            spin = Number.get(spinWheel(wheelType));
            spins.add(spin);
            boolean winner = spin.getWinningGroups().contains(bet);
            double odds = bet.getMultiplier();
            if (bet == NumberGroup.Green && wheelType == WheelType.OneZero)
                odds *= 2;
            if (winner) {
                return new GameResult(true, bet, odds, i, betAmount, totalBet, spins);
            }

            betAmount = bettingStrategy.getBetAmount(i, betAmount, spin);
            if (betAmount <= 0) {
                return new GameResult(false, bet, odds, i, lastbet, totalBet, spins);
            }
        }
    }

}
