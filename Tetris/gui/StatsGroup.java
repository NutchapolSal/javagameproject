package Tetris.gui;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;

public class StatsGroup {
    private JPanel statsPanel = new JPanel();
    private JLabel scoreCountText = new JLabel();
    private JLabel scoreText = new JLabel();
    private JLabel levelCountText = new JLabel();
    private JLabel levelText = new JLabel();
    private JLabel linesCountText = new JLabel();
    private JLabel linesText = new JLabel();
    private JLabel timeCountText = new JLabel();
    private JLabel timeText = new JLabel();

    public StatsGroup() {
        detailStatsLabels();

        GroupLayout statsPanelLayout = new GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
                statsPanelLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(scoreText)
                        .addComponent(scoreCountText)
                        .addComponent(levelText)
                        .addComponent(levelCountText)
                        .addComponent(linesText)
                        .addComponent(linesCountText)
                        .addComponent(timeText)
                        .addComponent(timeCountText));
        statsPanelLayout.setVerticalGroup(statsPanelLayout.createSequentialGroup()
                .addComponent(scoreText)
                .addComponent(scoreCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(levelText)
                .addComponent(levelCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(linesText)
                .addComponent(linesCountText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(timeText)
                .addComponent(timeCountText));
    }

    private void detailStatsLabels() {
        scoreText.setText("Score");
        scoreCountText.setFont(scoreCountText.getFont().deriveFont(
                scoreCountText.getFont().getStyle() | Font.BOLD, scoreCountText.getFont().getSize() + 7));
        scoreCountText.setText("0");

        levelText.setText("Level");

        levelCountText.setFont(levelCountText.getFont().deriveFont(
                levelCountText.getFont().getStyle() | Font.BOLD, levelCountText.getFont().getSize() + 7));
        levelCountText.setText("0");

        linesText.setText("Lines");

        linesCountText.setFont(linesCountText.getFont().deriveFont(
                linesCountText.getFont().getStyle() | Font.BOLD, linesCountText.getFont().getSize() + 7));
        linesCountText.setText("0");

        timeText.setText("Time");

        timeCountText.setFont(timeCountText.getFont().deriveFont(
                timeCountText.getFont().getStyle() | Font.BOLD, timeCountText.getFont().getSize() + 7));
        timeCountText.setText("0:00.00");
    }

    public JPanel getPanel() {
        return statsPanel;
    }

    public JLabel getScoreCountText() {
        return scoreCountText;
    }

    public JLabel getLevelCountText() {
        return levelCountText;
    }

    public JLabel getLinesCountText() {
        return linesCountText;
    }

    public JLabel getTimeCountText() {
        return timeCountText;
    }

}
