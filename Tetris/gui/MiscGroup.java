package Tetris.gui;

import Tetris.settings.ControlScheme;
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

    public void updateControlSchemeText(ControlScheme controlScheme, boolean sonicDrop) {
        String newControlText = "";
        switch (controlScheme) {
            case WASD:
                newControlText = "<html>\nA D - Move<br>\nS - ";
                break;

            case Classic:
                newControlText = "<html>\n⬅ ➡ - Move<br>\n⬇ - ";
                break;
            case SlashBracket:
                newControlText = "<html>\nA D - Move<br>\nS - ";
                break;
        }
        newControlText += sonicDrop ? "Sonic" : "Soft";
        switch (controlScheme) {
            case WASD:
                newControlText += " Drop<br>\nW - Hard Drop<br>\nR - Rotate<br>\nF - Hold";
                break;

            case Classic:
                newControlText += " Drop<br>\n⬆ - Hard Drop<br>\nZ - Rotate CCW<br>\nX - Rotate CW<br>\nC - Hold";
                break;
            case SlashBracket:
                newControlText += " Drop<br>\nW - Hard Drop<br>\n/ - Rotate CCW<br>\n[ - Rotate Flip<br>\n] - Rotate CW<br>\nShift - Hold";
                break;
        }
        controlsText.setText(newControlText);
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
