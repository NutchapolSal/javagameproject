package Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class PlayfieldPanel extends MinoPanel {
    private ObjectDataGrid<BlockWithConnection> renderBlocks = new ObjectDataGrid<>(PANEL_WIDTH_BLOCKS,
            PANEL_HEIGHT_BLOCKS);
    private PlayerRenderData pdr = null;
    private double playerLockProgress;
    private CalloutLabel callout;
    private MinoColor playerOverrideColor;

    public void setRenderBlocks(ObjectDataGrid<BlockWithConnection> renderBlocks) {
        this.renderBlocks = renderBlocks;
    }

    public void setPlayerRenderData(PlayerRenderData pdr, double playerLockProgress) {
        this.pdr = pdr;
        this.playerLockProgress = playerLockProgress;
        this.playerOverrideColor = null;

    }

    public void setPlayerOverrideColor(MinoColor playerOverrideColor) {
        this.playerOverrideColor = playerOverrideColor;
    }

    public PlayfieldPanel(BlockSkinManager blockSkinManager) {
        super(blockSkinManager, 10, 20);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        callout = new CalloutLabel();
        callout.setHorizontalAlignment(SwingConstants.CENTER);
        callout.setFont(callout.getFont().deriveFont(callout.getFont().getStyle() | Font.BOLD,
                callout.getFont().getSize() + 6));
        callout.setHorizontalAlignment(SwingConstants.CENTER);

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
        for (int y = 0; y < PANEL_HEIGHT_BLOCKS; y++) {
            for (int x = 0; x < PANEL_WIDTH_BLOCKS; x++) {
                BlockWithConnection block = renderBlocks.getAtPos(x, y);
                if (block == null) {
                    continue;
                }
                // boolean up = y < PANEL_HEIGHT_BLOCKS - 1 && renderBlocks.getAtPos(x, y + 1)
                // != null;
                // boolean right = x < PANEL_WIDTH_BLOCKS - 1 && renderBlocks.getAtPos(x + 1, y)
                // != null;
                // boolean down = 0 < y && renderBlocks.getAtPos(x, y - 1) != null;
                // boolean left = 0 < x && renderBlocks.getAtPos(x - 1, y) != null;
                paintMinoBlock(g, x, y, block);
                // System.out.printf("%s%s%s%s%n", up ? "u" : " ",
                // right ? "r" : " ",
                // down ? "d" : " ",
                // left ? "l" : " ");
            }
        }

        if (pdr == null) {
            return;
        }

        for (int y = 0; y < pdr.blocks.getHeight(); y++) {
            for (int x = 0; x < pdr.blocks.getWidth(); x++) {
                if (pdr.blocks.getAtPos(x, y) == null) {
                    continue;
                }
                BlockWithConnection block = pdr.blocks.getAtPos(x, y);
                MinoColor mc = block.getMinoColor();
                if (playerOverrideColor != null) {
                    mc = playerOverrideColor;
                }
                paintMinoBlock(g, x + pdr.x, y + pdr.y, block, mc);
                setOpacity(0.3);
                paintMinoBlock(g, x + pdr.x, y + pdr.shadowY, block, mc);
                setOpacity(playerLockProgress * 0.75);
                paintMinoBlock(g, x + pdr.x, y + pdr.y, block, MinoColor.Gray);
                setOpacity(1);
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
