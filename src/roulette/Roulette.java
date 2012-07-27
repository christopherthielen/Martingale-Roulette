package roulette;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * GUI and main entry point
 */
public class Roulette {
    BettingStrategy bettingStrategy;
    private boolean showRolls = false;
    private static boolean DEBUG = true;
    DefaultTableModel tableModel = new DefaultTableModel();

    JTable table = new JTable(tableModel);
    JFrame frame = new JFrame("Roulette");

    JLabel spinsLabel = new JLabel("Total Spins");
    JLabel betLabel = new JLabel("Total Bet");
    JLabel winlossLabel = new JLabel("Win/Loss");
    JLabel spins = new JLabel("0");
    JLabel bet = new JLabel("0");
    JLabel winloss = new JLabel("0");

    JTextField initialBet = new JTextField("0.25");
    JTextField maxBet = new JTextField("50");
    JTextField multipler = new JTextField("2.0");
    JTextField delay = new JTextField("2");
    JTextField denomination = new JTextField("0.25");
    JTextField games = new JTextField("25");
    JCheckBox neverGiveUp = new JCheckBox("Never Give Up!");

    JComboBox board = new JComboBox();
    ButtonGroup bg = new ButtonGroup();


    public static void main(String[] args) {
        Roulette r = new Roulette(new MartingaleStrategy(0.25, 512.0, 2, 3, 0.25, false));
        if (DEBUG)
            r.showRolls = true;
    }

    public Roulette(BettingStrategy bettingStrategy) {
        this.bettingStrategy = bettingStrategy;

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

        for (JLabel jabel : new JLabel[]{spinsLabel, betLabel, winlossLabel, spins, bet, winloss}) {
            jabel.setFont(jabel.getFont().deriveFont(25.0f));
        }
        status.add(spinsLabel);
        status.add(betLabel);
        status.add(winlossLabel);
        status.add(spins);
        status.add(bet);
        status.add(winloss);


        for (NumberGroup group : NumberGroup.values()) {
            JRadioButton button = new JRadioButton(group.name());
            betPanel.add(button);
            bg.add(button);
        }

        initialBet.setToolTipText("Starting bet during each game");
        initialBet.setColumns(4);
        strategyPanel.add(new JLabel("Initial Bet:"));
        strategyPanel.add(initialBet);

        maxBet.setToolTipText("Maximum you are willing to\nbet (and maximum table bet)");
        maxBet.setColumns(6);
        strategyPanel.add(new JLabel("Max Bet:"));
        strategyPanel.add(maxBet);

        multipler.setToolTipText("When \"doubling up\", this\nmultiplier is applied");
        multipler.setColumns(4);
        strategyPanel.add(new JLabel("Multiplier:"));
        strategyPanel.add(multipler);

        delay.setToolTipText("How many times your bet\nloses before you \"double\nup\" (i.e., apply\nthe multiplier)");
        delay.setColumns(4);
        strategyPanel.add(new JLabel("Losses before multiplying:"));
        strategyPanel.add(delay);

        denomination.setToolTipText("The smallest chip you can bet");
        denomination.setColumns(6);
        strategyPanel.add(new JLabel("Lowest Denomination:"));
        strategyPanel.add(denomination);

        games.setToolTipText("Number of games to simulate.\nEach game runs until\nyour bet is a winner or\nyour strategy gives up.");
        games.setColumns(6);
        strategyPanel.add(new JLabel("Games:"));
        strategyPanel.add(games);

        neverGiveUp.setToolTipText("Keep betting max bet until you win");
        strategyPanel.add(neverGiveUp);

        DefaultComboBoxModel model = (DefaultComboBoxModel) board.getModel();
        for (WheelType type : EnumSet.allOf(WheelType.class)) {
            model.addElement(type.getDescription());
        }
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
                component.setForeground((Double.parseDouble((String) value) > 0 ? new Color(45, 150, 45) : Double.parseDouble((String) value) == 0 ? Color.BLACK : Color.RED));
                return component;    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        play.addActionListener(new GameSessionRunner(this));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setBettingStrategy(BettingStrategy bettingStrategy) {
        this.bettingStrategy = bettingStrategy;
    }

    public void recordResult(long spinsL, double betD, double winlossD, double runningWinLossD, GameResult gameResult) {
        java.util.List<String> spinList = new ArrayList<String>();
        for (Number number : gameResult.getSpins()) {
            spinList.add(number.getShortString());
        }
        DecimalFormat fmt = new DecimalFormat("0.00");

        Vector<String> v = new Vector<String>(5);
        v.add((gameResult.win ? "WIN" : "LOSE"));
        v.add(String.valueOf(gameResult.iterations) + ": " + spinList);
        v.add(fmt.format(gameResult.lastBet));
        v.add(fmt.format(gameResult.totalBet));
        v.add(fmt.format(gameResult.totalWinnings));
        v.add(fmt.format(runningWinLossD));
        tableModel.addRow(v);

        spins.setText(String.valueOf(spinsL));
        bet.setText(String.valueOf(betD));
        winloss.setText(String.valueOf(winlossD));
        winloss.setForeground(winlossD > 0 ? Color.green : Color.red);
    }

}
