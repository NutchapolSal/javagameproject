package Tetris.gui;

import Tetris.leaderboard.Leaderboard.LeaderboardEntry;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LeaderboardFrame {
    private JFrame f = new JFrame("Leaderboard");
    private LeaderboardEntryLabels[] entries = new LeaderboardEntryLabels[19];

    // private Component parent;

    public LeaderboardFrame(Component parent) {
        // this.parent = parent;

        f.setSize(350, 1080);
        f.setExtendedState(JFrame.MAXIMIZED_VERT);
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f.setAutoRequestFocus(false);
        f.setLocation(0, 0);
        detailComponents();

    }

    private void detailComponents() {
        var contentPane = f.getContentPane();

        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);

        var vGroup = layout.createSequentialGroup();
        vGroup.addGap(15);
        var indexHGroup = layout.createParallelGroup();
        var nameHGroup = layout.createParallelGroup();
        var scoreHGroup = layout.createParallelGroup();
        var scoreCountHGroup = layout.createParallelGroup();
        var lineHGroup = layout.createParallelGroup();
        var lineCountHGroup = layout.createParallelGroup();
        var timeHGroup = layout.createParallelGroup();
        var timeCountHGroup = layout.createParallelGroup();

        for (int i = 0; i < entries.length; i++) {
            if (0 < i) {
                vGroup.addPreferredGap(ComponentPlacement.RELATED);
            }
            entries[i] = new LeaderboardEntryLabels(layout,
                    vGroup,
                    indexHGroup,
                    nameHGroup,
                    scoreHGroup,
                    scoreCountHGroup,
                    lineHGroup,
                    lineCountHGroup,
                    timeHGroup,
                    timeCountHGroup);
        }

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGap(15)
                        .addGroup(indexHGroup)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup()
                                .addGroup(nameHGroup)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(scoreHGroup)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(scoreCountHGroup)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(lineHGroup)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(lineCountHGroup)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(timeHGroup)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addGroup(timeCountHGroup))));
    }

    public void setVisible(boolean b) {
        f.setVisible(b);
    }

    public void updateLeaderboard(LeaderboardEntry[] lbe) {
        for (int i = 0; i < entries.length; i++) {
            var elem = i < lbe.length ? lbe[i] : null;
            entries[i].setData(i + 1, elem);
        }
    }
}
