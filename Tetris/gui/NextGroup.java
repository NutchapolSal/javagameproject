package Tetris.gui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class NextGroup {
    private OneMinoPanel[] nextMinos = new OneMinoPanel[5];
    private JPanel nextPanel = new JPanel();
    private JLabel nextText = new JLabel();

    public NextGroup(BlockSkinManager blockSkinManager, Alignment alignment) {
        nextText.setText("Next");

        GroupLayout nextPanelLayout = new GroupLayout(nextPanel);
        nextPanel.setLayout(nextPanelLayout);
        var horizGroup = nextPanelLayout.createParallelGroup(alignment)
                .addComponent(nextText);
        var vertGroup = nextPanelLayout.createSequentialGroup()
                .addComponent(nextText);

        for (int i = 0; i < nextMinos.length; i++) {
            nextMinos[i] = new OneMinoPanel(blockSkinManager);
            horizGroup.addComponent(nextMinos[i]);

            vertGroup.addPreferredGap(ComponentPlacement.RELATED);
            vertGroup.addComponent(nextMinos[i]);
        }
        nextPanelLayout.setHorizontalGroup(horizGroup);
        nextPanelLayout.setVerticalGroup(vertGroup);
    }

    public OneMinoPanel[] getNextMinos() {
        return nextMinos;
    }

    public JPanel getPanel() {
        return nextPanel;
    }
}
