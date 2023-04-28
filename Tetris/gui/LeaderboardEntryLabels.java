package Tetris.gui;

import Tetris.leaderboard.Leaderboard.LeaderboardEntry;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;

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

        vGroup.addGroup(layout.createParallelGroup(Alignment.CENTER)
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

        var cf = CustomFontChecker.getFontChecker("Tw Cen MT");

        Map<TextAttribute, Object> nameDeriveMap = new HashMap<>();
        nameDeriveMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        if (cf.available) {
            nameDeriveMap.put(TextAttribute.FAMILY, cf.fontName);
        }
        nameDeriveMap.put(TextAttribute.SIZE,
                nameLabel.getFont().getSize() + 8 + (cf.available ? cf.plusSize : 0));
        nameLabel.setFont(nameLabel.getFont().deriveFont(nameDeriveMap));

        Map<TextAttribute, Object> indexDeriveMap = new HashMap<>();
        indexDeriveMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        if (cf.available) {
            indexDeriveMap.put(TextAttribute.FAMILY, cf.fontName);
        }
        indexDeriveMap.put(TextAttribute.SIZE,
                indexLabel.getFont().getSize() + 18 + (cf.available ? cf.plusSize : 0));
        indexLabel.setFont(indexLabel.getFont().deriveFont(indexDeriveMap));

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
        String nameA = lb.nameP1 != null ? lb.nameP1 : "anon";
        String nameB = lb.nameP2 != null ? lb.nameP2 : "anon";
        nameLabel.setText(nameA + " x " + nameB);
        scoreCountLabel.setText(lb.score == -1 ? "?" : Integer.toString(lb.score));
        lineCountLabel.setText(lb.lines == -1 ? "?" : Integer.toString(lb.lines));
        timeCountLabel.setText(lb.time == -1 ? "?"
                : String.format("%.0f:%05.2f", Math.floor(lb.time / (1000d * 60)),
                        (lb.time / 1000d) % 60));
    }
}
