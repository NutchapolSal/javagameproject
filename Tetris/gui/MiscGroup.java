package Tetris.gui;

import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class MiscGroup {
    private JPanel miscPanel = new JPanel();
    private JButton newGameButton = new JButton();
    private JLabel controlsText = new JLabel();

    public MiscGroup() {
        newGameButton.setFocusable(false);

        newGameButton.setText("New Game");
        controlsText.setText("");
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

    public JPanel getPanel() {
        return miscPanel;
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JLabel getControlsText() {
        return controlsText;
    }
}
