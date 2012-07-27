package roulette;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Roulette {
    private Strategy strategy;
    private boolean showRolls = false;
    private static boolean DEBUG = true;
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table = new JTable(tableModel);
    JFrame frame = new JFrame("Roulette");

    public static void main(String[] args) {
        Roulette r = new Roulette(new BasicStrategy(0.25, 512.0, 2, 3, 0.25, false));
        if (DEBUG)
            r.showRolls = true;
    }

    public Roulette(Strategy strategy) {
        this.strategy = strategy;

        Container cp = frame.getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel(new BorderLayout(5, 5));
        JPanel panel2 = new JPanel(new BorderLayout());
        JPanel betPanel = new JPanel(new GridLayout(3, 6) );
        betPanel.setBorder(new TitledBorder("Bet On"));
        JPanel strategyPanel = new JPanel(new GridLayout(0, 6));
        strategyPanel.setBorder(new TitledBorder("Strategy"));

        JPanel gamePanel = new JPanel(new FlowLayout());
        gamePanel.setBorder(new TitledBorder("Game"));
        JPanel status = new JPanel(new GridLayout(2, 3));

        panel1.add(status, BorderLayout.NORTH);
        panel1.add(betPanel, BorderLayout.CENTER);

        panel2.add(strategyPanel, BorderLayout.NORTH);
        panel2.add(gamePanel, BorderLayout.SOUTH);
        panel1.add(panel2, BorderLayout.SOUTH);

        cp.add(panel1, BorderLayout.NORTH);
        cp.add(new JScrollPane(table));

        JLabel jLabel = new JLabel("Total Spins");
        JLabel jLabel1 = new JLabel("Total Bet");
        JLabel jLabel2 = new JLabel("Win/Loss");
        final JLabel spins = new JLabel("0");
        final JLabel bet = new JLabel("0");
        final JLabel winloss = new JLabel("0");
        for (JLabel jabel : new JLabel[]{jLabel, jLabel1, jLabel2, spins, bet, winloss}) {
            jabel.setFont(jabel.getFont().deriveFont(25.0f));
        }
        status.add(jLabel);
        status.add(jLabel1);
        status.add(jLabel2);
        status.add(spins);
        status.add(bet);
        status.add(winloss);


        final ButtonGroup bg = new ButtonGroup();
        for (NumberGroups group : NumberGroups.values()) {
            JRadioButton button = new JRadioButton(group.name());
            betPanel.add(button);
            bg.add(button);
        }

        final JTextField initialBet = new JTextField("0.25");
        initialBet.setToolTipText("Starting bet during each game");
        initialBet.setColumns(4);
        strategyPanel.add(new JLabel("Initial Bet:"));
        strategyPanel.add(initialBet);

        final JTextField maxBet = new JTextField("50");
        maxBet.setToolTipText("Maximum you are willing to\nbet (and maximum table bet)");
        maxBet.setColumns(6);
        strategyPanel.add(new JLabel("Max Bet:"));
        strategyPanel.add(maxBet);

        final JTextField multipler = new JTextField("2.0");
        multipler.setToolTipText("When \"doubling up\", this\nmultiplier is applied");
        multipler.setColumns(4);
        strategyPanel.add(new JLabel("Multiplier:"));
        strategyPanel.add(multipler);

        final JTextField delay = new JTextField("2");
        delay.setToolTipText("How many times your bet\nloses before you \"double\nup\" (i.e., apply\nthe multiplier)");
        delay.setColumns(4);
        strategyPanel.add(new JLabel("Losses before multiplying:"));
        strategyPanel.add(delay);

        final JTextField denomination = new JTextField("0.25");
        denomination.setToolTipText("The smallest chip you can bet");
        denomination.setColumns(6);
        strategyPanel.add(new JLabel("Lowest Denomination:"));
        strategyPanel.add(denomination);

        final JTextField games = new JTextField("25");
        games.setToolTipText("Number of games to simulate.\nEach game runs until\nyour bet is a winner or\nyour strategy gives up.");
        games.setColumns(6);
        strategyPanel.add(new JLabel("Games:"));
        strategyPanel.add(games);

        final JCheckBox neverGiveUp = new JCheckBox("Never Give Up!");
        neverGiveUp.setToolTipText("Keep betting max bet until you win");
        strategyPanel.add(neverGiveUp);

        EnumSet<BoardType> types = EnumSet.allOf(BoardType.class);
        List<String> boardTypes = new ArrayList<String>();
        for (BoardType type : types) {
            boardTypes.add(type.getDescription());
        }
        final JComboBox board = new JComboBox(boardTypes.toArray());
        board.setToolTipText("How many Greens (zeros) does the wheel/board have?");
        JButton play = new JButton("Play");
        gamePanel.add(new JLabel("Wheel:"));
        gamePanel.add(board);
        gamePanel.add(play);

        tableModel.addColumn("Win/Lose");
        tableModel.addColumn("Spins");
        tableModel.addColumn("Last Bet");
        tableModel.addColumn("Total Bet");
        tableModel.addColumn("Winnings");
        tableModel.addColumn("Running Total");

        TableColumn column = table.getColumn("Winnings");
        column.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setForeground((Double.parseDouble((String) value) > 0 ? new Color(45, 150, 45) : Double.parseDouble((String) value) == 0 ? Color.BLACK : Color.RED));
                return component;    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        column = table.getColumn("Running Total");
        column.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setForeground(( Double.parseDouble((String) value) > 0 ? new Color(45, 150, 45) : Double.parseDouble((String) value) == 0 ? Color.BLACK : Color.RED));
                return component;    //To change body of overridden methods use File | Settings | File Templates.
            }
        });
        System.out.println("column = " + column);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (tableModel.getRowCount() > 0) {
                    tableModel.removeRow(tableModel.getRowCount() - 1);
                }

                Double startingBetL = Double.valueOf(initialBet.getText());
                Double maxBetL = Double.valueOf(maxBet.getText());
                Double multL = Double.valueOf(multipler.getText());
                Long delayL = Long.valueOf(delay.getText());
                Double denominationL = Double.valueOf(denomination.getText());
                Boolean dontGiveUp = neverGiveUp.getModel().isSelected();
                setStrategy(new BasicStrategy(startingBetL, maxBetL, multL, delayL, denominationL, dontGiveUp));

                final Long iterations = Long.valueOf(games.getText());
                Enumeration<AbstractButton> elements = bg.getElements();
                while (elements.hasMoreElements()) {
                    AbstractButton button = elements.nextElement();
                    if (button.isSelected()) {
                        String text = button.getText();
                        final NumberGroups groups = NumberGroups.valueOf(text);
                            new Thread() {
                                @Override
                                public void run() {
                                    long spinsL = 0;
                                    double betD = 0;
                                    double winlossD = 0;
                                    double runningWinLossD = 0;
                                    for (int i = 0; i < iterations; i++) {
                                        BoardType boardType = BoardType.fromDescription((String) board.getSelectedItem());
                                        Results results = runGame(groups, boardType);
                                        runningWinLossD += results.totalWinnings;

                                        DecimalFormat fmt = new DecimalFormat("0.00");

                                        Vector<String> v = new Vector<String>(5);
                                        v.add((results.win ? "WIN" : "LOSE"));
                                        v.add(String.valueOf(results.iterations));
                                        v.add(fmt.format(results.lastBet));
                                        v.add(fmt.format(results.totalBet));
                                        v.add(fmt.format(results.totalWinnings));
                                        v.add(fmt.format(runningWinLossD));
                                        tableModel.addRow(v);

                                        spinsL += results.iterations;
                                        betD += results.totalBet;
                                        winlossD += results.totalWinnings;
                                        spins.setText(String.valueOf(spinsL));
                                        bet.setText(String.valueOf(betD));
                                        winloss.setText(String.valueOf(winlossD));
                                        winloss.setForeground(winlossD > 0 ? Color.green : Color.red);

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
        });


        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Results runGame(NumberGroups bet, BoardType boardType) {
        double totalBet = 0.0;
        double betAmount = strategy.getStartingBet();
        double lastbet = 0;

        Number rolled;
        int i = 0;
        while (true) {
            lastbet = betAmount;
            totalBet += betAmount;
            i++;
            rolled = Number.get(roll(boardType));
            boolean winner = rolled.getGroups().contains(bet);
            if (showRolls) {
                System.out.println("rolled = " + rolled + (winner ? " WIN: " + bet : ""));
            }
            double odds = bet.getMultiplier();
            if (bet == NumberGroups.Green && boardType == BoardType.OneZero)
                odds *= 2;
            if (winner) {
                return new Results(true, bet, odds, i, betAmount, totalBet);
            }

            betAmount = strategy.getBetAmount(i, betAmount, rolled);
            if (betAmount <= 0) {
                return new Results(false, bet, odds, i, lastbet, totalBet);
            }
        }
    }

    static SecureRandom secureRandom = new SecureRandom();

    public static int roll(BoardType boardType) {
        int mult = 0;
        switch (boardType) {
            case OneZero:
                mult = 37;
                break;
            case TwoZeros:
                mult = 38;
                break;
        }
        return (int) (secureRandom.nextDouble() * mult + 1);
    }
}
