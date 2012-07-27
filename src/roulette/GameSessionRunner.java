package roulette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: sone
* Date: 7/27/12
* Time: 1:56 PM
* To change this template use File | Settings | File Templates.
*/
class GameSessionRunner implements ActionListener {
    static SecureRandom secureRandom = new SecureRandom();
    private Roulette gui;

    public GameSessionRunner(Roulette gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        while (gui.tableModel.getRowCount() > 0) {
            gui.tableModel.removeRow(gui.tableModel.getRowCount() - 1);
        }

        Double startingBetL = Double.valueOf(gui.initialBet.getText());
        Double maxBetL = Double.valueOf(gui.maxBet.getText());
        Double multL = Double.valueOf(gui.multipler.getText());
        Long delayL = Long.valueOf(gui.delay.getText());
        Double denominationL = Double.valueOf(gui.denomination.getText());
        Boolean dontGiveUp = gui.neverGiveUp.getModel().isSelected();
        gui.setBettingStrategy(new MartingaleStrategy(startingBetL, maxBetL, multL, delayL, denominationL, dontGiveUp));

        final Long iterations = Long.valueOf(gui.games.getText());
        Enumeration<AbstractButton> elements = gui.bg.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.isSelected()) {
                String text = button.getText();
                final NumberGroup group = NumberGroup.valueOf(text);
                    new Thread() {
                        @Override
                        public void run() {
                            long spinsL = 0;
                            double betD = 0;
                            double winlossD = 0;
                            double runningWinLossD = 0;
                            for (int i = 0; i < iterations; i++) {
                                WheelType wheelType = WheelType.fromDescription((String) gui.board.getSelectedItem());
                                Results results = runGame(group, wheelType);
                                runningWinLossD += results.totalWinnings;

                                List<String> spins = new ArrayList<String>();
                                for (Number number : results.getSpins()) {
                                    spins.add(number.getShortString());
                                }
                                        DecimalFormat fmt = new DecimalFormat("0.00");

                                Vector<String> v = new Vector<String>(5);
                                v.add((results.win ? "WIN" : "LOSE"));
                                v.add(String.valueOf(results.iterations) + ": " + spins);
                                v.add(fmt.format(results.lastBet));
                                v.add(fmt.format(results.totalBet));
                                v.add(fmt.format(results.totalWinnings));
                                v.add(fmt.format(runningWinLossD));
                                gui.tableModel.addRow(v);

                                spinsL += results.iterations;
                                betD += results.totalBet;
                                winlossD += results.totalWinnings;
                                gui.spins.setText(String.valueOf(spinsL));
                                gui.bet.setText(String.valueOf(betD));
                                gui.winloss.setText(String.valueOf(winlossD));
                                gui.winloss.setForeground(winlossD > 0 ? Color.green : Color.red);

                                try {
                                    Thread.sleep(Math.min(100, 4000 / iterations));
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                            }
                        }
                    }.start();
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


    public Results runGame(NumberGroup bet, WheelType wheelType) {
        double totalBet = 0.0;
        double betAmount = gui.bettingStrategy.getStartingBet();
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
                return new Results(true, bet, odds, i, betAmount, totalBet, spins);
            }

            betAmount = gui.bettingStrategy.getBetAmount(i, betAmount, spin);
            if (betAmount <= 0) {
                return new Results(false, bet, odds, i, lastbet, totalBet, spins);
            }
        }
    }

}