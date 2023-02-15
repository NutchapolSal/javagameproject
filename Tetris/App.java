package Tetris;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Dimension;
import java.security.acl.Group;
import java.awt.Color;
import java.awt.Container;

public class App {
    private JFrame f;
    private JPanel centerPanel;
    private JLabel controlsText;
    private JPanel holdMino;
    private JPanel holdPanel;
    private JLabel holdText;
    private JPanel statsPanel;
    private Box.Filler leftFiller;
    private JLabel levelCountText;
    private JLabel levelText;
    private JLabel linesCountText;
    private JLabel linesText;
    private JButton newGameButton;
    private JPanel nextMino1;
    private JPanel nextMino2;
    private JPanel nextMino3;
    private JPanel nextMino4;
    private JPanel nextMino5;
    private JPanel nextPanel;
    private JLabel nextText;
    private JPanel playfield;
    private Box.Filler rightFiller;
    private JLabel timeCountText;
    private JLabel timeText;
    private JPanel miscPanel;

    public App() {
        f = new JFrame("Tetris");
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        detailComponents();
        f.setVisible(true);
    }

    private void detailComponents() {
        leftFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        createCenterPanel();
        rightFiller = new Box.Filler(
                new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));

        Container contentPane = f.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(leftFiller);
        contentPane.add(centerPanel);
        contentPane.add(rightFiller);

    }

    private void createCenterPanel() {
        centerPanel = new JPanel();
        createPlayfield();
        createStatPanel();
        createNextPanel();
        createHoldPanel();
        createMiscPanel();

        GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(centerPanelLayout.createSequentialGroup()
                .addGroup(centerPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(holdPanel)
                        .addComponent(statsPanel))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(playfield)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(centerPanelLayout.createParallelGroup()
                        .addComponent(nextPanel)
                        .addComponent(miscPanel)

                ));
        centerPanelLayout.setVerticalGroup(centerPanelLayout.createParallelGroup(Alignment.LEADING, false)
                .addGroup(centerPanelLayout.createSequentialGroup()
                        .addComponent(holdPanel)
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
                                Integer.MAX_VALUE)
                        .addComponent(statsPanel))
                .addComponent(playfield)
                .addGroup(centerPanelLayout.createSequentialGroup()
                        .addComponent(nextPanel)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(miscPanel)

                ));
    }

    private void createHoldPanel() {
        holdPanel = new JPanel();
        holdMino = new MinoPanel();
        holdText = new JLabel();

        holdText.setText("Hold");

        GroupLayout holdPanelLayout = new GroupLayout(holdPanel);
        holdPanel.setLayout(holdPanelLayout);
        holdPanelLayout.setHorizontalGroup(holdPanelLayout.createParallelGroup(Alignment.TRAILING)
                .addComponent(holdText)
                .addComponent(holdMino));
        holdPanelLayout.setVerticalGroup(holdPanelLayout.createSequentialGroup()
                .addComponent(holdText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(holdMino));
    }

    private void createMiscPanel() {
        miscPanel = new JPanel();
        newGameButton = new JButton();
        controlsText = new JLabel();

        newGameButton.setText("New Game");
        controlsText.setText("<html>\nA/D - Move<br>\nS - Soft Drop<br>\nW - Hard Drop<br>\nR - Rotate<br>\nF - Hold");
        controlsText.setVerticalAlignment(SwingConstants.TOP);
        controlsText.setPreferredSize(new Dimension(80, 1));

        GroupLayout miscPanelLayout = new GroupLayout(miscPanel);
        miscPanel.setLayout(miscPanelLayout);
        miscPanelLayout.setHorizontalGroup(miscPanelLayout.createParallelGroup()
                .addComponent(newGameButton)
                .addComponent(controlsText, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
        miscPanelLayout.setVerticalGroup(miscPanelLayout.createSequentialGroup()
                .addComponent(newGameButton)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(controlsText));

    }

    private void createNextPanel() {
        nextPanel = new JPanel();
        nextText = new JLabel();
        nextMino1 = new MinoPanel();
        nextMino2 = new MinoPanel();
        nextMino3 = new MinoPanel();
        nextMino4 = new MinoPanel();
        nextMino5 = new MinoPanel();

        nextText.setText("Next");

        GroupLayout nextPanelLayout = new GroupLayout(nextPanel);
        nextPanel.setLayout(nextPanelLayout);
        nextPanelLayout.setHorizontalGroup(nextPanelLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(nextText)
                .addComponent(nextMino1)
                .addComponent(nextMino2)
                .addComponent(nextMino3)
                .addComponent(nextMino4)
                .addComponent(nextMino5));
        nextPanelLayout.setVerticalGroup(nextPanelLayout.createSequentialGroup()
                .addComponent(nextText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nextMino1)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nextMino2)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nextMino3)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nextMino4)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(nextMino5));

    }

    private void createPlayfield() {
        playfield = new PlayfieldPanel();
    }

    private void createStatPanel() {
        statsPanel = new JPanel();
        createStatsLabels();

        GroupLayout statsPanelLayout = new GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
                statsPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(levelText)
                        .addComponent(levelCountText)
                        .addComponent(linesText)
                        .addComponent(linesCountText)
                        .addComponent(timeText)
                        .addComponent(timeCountText));
        statsPanelLayout.setVerticalGroup(statsPanelLayout.createSequentialGroup()
                .addComponent(levelText)
                .addComponent(levelCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(linesText)
                .addComponent(linesCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(timeText)
                .addComponent(timeCountText));

    }

    private void createStatsLabels() {
        levelText = new JLabel();
        levelCountText = new JLabel();
        linesText = new JLabel();
        linesCountText = new JLabel();
        timeText = new JLabel();
        timeCountText = new JLabel();

        levelText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        levelText.setText("Level");

        levelCountText.setFont(levelCountText.getFont().deriveFont(
                levelCountText.getFont().getStyle() | java.awt.Font.BOLD, levelCountText.getFont().getSize() + 4));
        levelCountText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        levelCountText.setText("0");

        linesText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        linesText.setText("Lines");

        linesCountText.setFont(linesCountText.getFont().deriveFont(
                linesCountText.getFont().getStyle() | java.awt.Font.BOLD, linesCountText.getFont().getSize() + 4));
        linesCountText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        linesCountText.setText("0");

        timeText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timeText.setText("Time");

        timeCountText.setFont(timeCountText.getFont().deriveFont(
                timeCountText.getFont().getStyle() | java.awt.Font.BOLD, timeCountText.getFont().getSize() + 4));
        timeCountText.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timeCountText.setText("00.00");
    }
}
