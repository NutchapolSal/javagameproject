package Tetris.gui;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class HoldGroupMock {
    private OneMinoPanel holdMino;
    private JPanel holdPanel = new JPanel();
    private JLabel holdText = new JLabel();

    public HoldGroupMock(BlockSkinManager blockSkinManager) {
        holdMino = new OneRandomMinoPanel(blockSkinManager);

        holdText.setText("Hold");

        GroupLayout holdPanelLayout = new GroupLayout(holdPanel);
        holdPanel.setLayout(holdPanelLayout);
        holdPanelLayout.setHorizontalGroup(holdPanelLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(holdText)
                .addComponent(holdMino));
        holdPanelLayout.setVerticalGroup(holdPanelLayout.createSequentialGroup()
                .addComponent(holdText)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(holdMino));
    }

    public OneMinoPanel getHoldMino() {
        return holdMino;
    }

    public JPanel getPanel() {
        return holdPanel;
    }
}
