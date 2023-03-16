package Tetris;

import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Font;

public class PlayfieldPanel extends MinoPanel {
    private ObjectDataGrid<MinoColor> renderBlocks = new ObjectDataGrid<>(PANEL_WIDTH_BLOCKS, PANEL_HEIGHT_BLOCKS);
    private PlayerRenderData pdr = null;
    private double playerLockProgress;
    private CalloutLabel callout;

    public void setRenderBlocks(ObjectDataGrid<MinoColor> renderBlocks) {
        this.renderBlocks = renderBlocks;
    }

    public void setPlayerRenderData(PlayerRenderData pdr, double playerLockProgress) {
        this.pdr = pdr;
        this.playerLockProgress = playerLockProgress;
    }

    public PlayfieldPanel() {
        super(10, 20);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        callout = new CalloutLabel();
        callout.setHorizontalAlignment(SwingConstants.CENTER);
        callout.setFont(callout.getFont().deriveFont(callout.getFont().getStyle() | Font.BOLD,
                callout.getFont().getSize() + 6));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addComponent(callout)
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(PREF_H / 6)
                        .addComponent(callout));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        for (int y = 0; y < PANEL_HEIGHT_BLOCKS; y++) {
            for (int x = 0; x < PANEL_WIDTH_BLOCKS; x++) {
                MinoColor currColor = renderBlocks.getAtPos(x, y);
                if (currColor == null) {
                    continue;
                }
                paintMinoBlock(g, x, y, currColor);
            }
        }

        if (pdr == null) {
            return;
        }

        for (int y = 0; y < pdr.blocks.getHeight(); y++) {
            for (int x = 0; x < pdr.blocks.getWidth(); x++) {
                MinoColor currColor = pdr.blocks.getAtPos(x, y);
                if (currColor == null) {
                    continue;
                }
                paintMinoBlock(g, x + pdr.x, y + pdr.y, currColor);
                paintMinoBlock(g, x + pdr.x, y + pdr.shadowY, currColor, 0.3);
                paintMinoBlock(g, x + pdr.x, y + pdr.y, MinoColor.Gray, playerLockProgress * 0.75);
            }
        }
    }

    public void startAnimation(String s, boolean isFadeOut) {
        callout.startAnimation(s, isFadeOut);
    }

    public void startAnimation(String s) {
        callout.startAnimation(s);
    }
}
