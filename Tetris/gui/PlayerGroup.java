package Tetris.gui;

import Tetris.settings.ControlScheme;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PlayerGroup {
    private HoldGroup holdGroup;
    private NextGroup nextGroup;
    private JLabel controlsText = new JLabel();
    private JPanel panel = new JPanel();
    private Alignment alignment;
    private int playerIndex;
    private ControlScheme controlScheme = ControlScheme.WASD;
    private boolean controlSchemeSonicDrop;

    public PlayerGroup(int playerIndex, BlockSkinManager blockSkinManager, Alignment alignment) {
        this.playerIndex = playerIndex;
        this.alignment = alignment;
        holdGroup = new HoldGroup(blockSkinManager, alignment);
        nextGroup = new NextGroup(blockSkinManager, alignment);

        controlsText.setText("");
        controlsText.setVerticalAlignment(SwingConstants.TOP);
        if (alignment == Alignment.TRAILING) {
            controlsText.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        controlsText.setPreferredSize(new Dimension(80, 1));

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        SequentialGroup hGroup = layout.createSequentialGroup();
        if (alignment != Alignment.TRAILING) {
            hGroup.addComponent(nextGroup.getPanel());
            hGroup.addPreferredGap(ComponentPlacement.RELATED);
        }
        hGroup.addGroup(layout.createParallelGroup(alignment, false)
                .addComponent(holdGroup.getPanel())
                .addComponent(controlsText));
        if (alignment == Alignment.TRAILING) {
            hGroup.addPreferredGap(ComponentPlacement.RELATED);
            hGroup.addComponent(nextGroup.getPanel());
        }

        ParallelGroup vGroup = layout.createParallelGroup(Alignment.LEADING);
        if (alignment != Alignment.TRAILING) {
            vGroup.addComponent(nextGroup.getPanel());
        }
        vGroup.addGroup(layout.createSequentialGroup()
                .addComponent(holdGroup.getPanel())
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(controlsText));
        if (alignment == Alignment.TRAILING) {
            vGroup.addComponent(nextGroup.getPanel());
        }

        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);

        updateControlSchemeText();
    }

    public void setControlScheme(ControlScheme controlScheme) {
        this.controlScheme = controlScheme;
        updateControlSchemeText();
    }

    public void setSonicDrop(boolean sonicDrop) {
        this.controlSchemeSonicDrop = sonicDrop;
        updateControlSchemeText();
    }

    private void updateControlSchemeText() {
        String newControlText = "<html>" + (alignment == Alignment.TRAILING ? "<body style='text-align: right'>" : "");

        ControlsTextData[] ctds = ControlsTextData.getControlsTexts(playerIndex, controlScheme, controlSchemeSonicDrop);

        boolean start = true;
        for (ControlsTextData ctd : ctds) {
            if (start) {
                start = false;
            } else {
                newControlText += "<br>";
            }
            if (alignment == Alignment.TRAILING) {
                newControlText += ctd.name + " - " + ctd.button;
            } else {
                newControlText += ctd.button + " - " + ctd.name;
            }
        }

        controlsText.setText(newControlText);
    }

    public JPanel getPanel() {
        return panel;
    }

    public HoldGroup getHoldGroup() {
        return holdGroup;
    }

    public NextGroup getNextGroup() {
        return nextGroup;
    }
}
