package Tetris.gui;

import Tetris.leaderboard.Leaderboard.LeaderboardEntry;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Group;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LeaderboardEntryLabels {
    private JLabel indexLabel = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel scoreLabel = new JLabel();
    private JLabel scoreCountLabel = new JLabel();
    private JLabel lineLabel = new JLabel();
    private JLabel lineCountLabel = new JLabel();
    private JLabel timeLabel = new JLabel();
    private JLabel timeCountLabel = new JLabel();

    public LeaderboardEntryLabels(GroupLayout layout,
            Group vGroup,
            Group indexHGroup,
            Group nameHGroup,
            Group scoreHGroup,
            Group scoreCountHGroup,
            Group lineHGroup,
            Group lineCountHGroup,
            Group timeHGroup,
            Group timeCountHGroup) {
        detailComponents();

        indexHGroup.addComponent(indexLabel);
        nameHGroup.addComponent(nameLabel);
        scoreHGroup.addComponent(scoreLabel);
        scoreCountHGroup.addComponent(scoreCountLabel);
        lineHGroup.addComponent(lineLabel);
        lineCountHGroup.addComponent(lineCountLabel);
        timeHGroup.addComponent(timeLabel);
        timeCountHGroup.addComponent(timeCountLabel);

        vGroup.addGroup(layout.createParallelGroup()
                .addComponent(indexLabel)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(scoreLabel)
                                .addComponent(scoreCountLabel)
                                .addComponent(lineLabel)
                                .addComponent(lineCountLabel)
                                .addComponent(timeLabel)
                                .addComponent(timeCountLabel))));
    }

    private void detailComponents() {
        setData(0, null);

        nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getStyle() | java.awt.Font.BOLD,
                nameLabel.getFont().getSize() + 8));

        indexLabel.setFont(indexLabel.getFont().deriveFont(indexLabel.getFont().getStyle() | java.awt.Font.BOLD,
                indexLabel.getFont().getSize() + 18));

        scoreCountLabel.setFont(
                scoreCountLabel.getFont().deriveFont(scoreCountLabel.getFont().getStyle() | java.awt.Font.BOLD));
        lineCountLabel
                .setFont(lineCountLabel.getFont().deriveFont(lineCountLabel.getFont().getStyle() | java.awt.Font.BOLD));
        timeCountLabel
                .setFont(timeCountLabel.getFont().deriveFont(timeCountLabel.getFont().getStyle() | java.awt.Font.BOLD));

    }

    public void setData(int index, LeaderboardEntry lb) {
        if (lb == null) {
            indexLabel.setText(null);
            nameLabel.setText(null);
            scoreLabel.setText(null);
            lineLabel.setText(null);
            timeLabel.setText(null);
            scoreCountLabel.setText(null);
            lineCountLabel.setText(null);
            timeCountLabel.setText(null);
            return;
        }
        indexLabel.setText(Integer.toString(index));
        scoreLabel.setText("Score");
        lineLabel.setText("Lines");
        timeLabel.setText("Time");
        nameLabel.setText(lb.nameP1 + " x " + lb.nameP2);
        scoreCountLabel.setText(lb.score == -1 ? "?" : Integer.toString(lb.score));
        lineCountLabel.setText(lb.lines == -1 ? "?" : Integer.toString(lb.lines));
        timeCountLabel.setText(lb.time == -1 ? "?"
                : String.format("%.0f:%05.2f", Math.floor(lb.time / (1000d * 60)),
                        (lb.time / 1000d) % 60));
    }
}
