package Tetris.gui;

import Tetris.data.BlockWithConnection;
import Tetris.data.ObjectDataGrid;
import Tetris.data.PlayerGuiData;
import Tetris.data.PlayerRenderData;
import Tetris.data.mino.MinoColor;
import Tetris.gameplay.Playfield;
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
    private PlayerGuiData[] pgds;
    private CalloutLabel callout;
    private MinoColor playerOverrideColor;

    public void setRenderBlocks(ObjectDataGrid<BlockWithConnection> renderBlocks) {
        this.renderBlocks = renderBlocks;
        setBlocksSize(this.renderBlocks.getWidth(), this.renderBlocks.getHeight() - Playfield.FIELD_HEIGHT_BUFFER);
    }

    public void setPlayerGuiDatas(PlayerGuiData[] pgds) {
        this.pgds = pgds;
        this.playerOverrideColor = null;
    }

    public void setPlayerOverrideColor(MinoColor playerOverrideColor) {
        this.playerOverrideColor = playerOverrideColor;
    }

    public PlayfieldPanel(BlockSkinManager blockSkinManager) {
        super(blockSkinManager, 20, 20);
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
        setOpacity(1);
        for (int y = 0; y < PANEL_HEIGHT_BLOCKS; y++) {
            for (int x = 0; x < PANEL_WIDTH_BLOCKS; x++) {
                BlockWithConnection block = renderBlocks.getAtPos(x, y);
                if (block == null) {
                    continue;
                }
                paintMinoBlock(g, x, y, block);
            }
        }

        if (pgds == null) {
            return;
        }

        for (PlayerGuiData pgd : pgds) {
            if (pgd == null) {
                continue;
            }
            PlayerRenderData pdr = pgd.pdr;
            if (pdr == null) {
                continue;
            }
            for (int y = pdr.blocks.getHeight() - 1; 0 <= y; y--) {
                for (int x = 0; x < pdr.blocks.getWidth(); x++) {
                    if (pdr.blocks.getAtPos(x, y) == null) {
                        continue;
                    }
                    BlockWithConnection block = pdr.blocks.getAtPos(x, y);
                    MinoColor mc = block.getMinoColor();
                    if (playerOverrideColor != null) {
                        mc = playerOverrideColor;
                    }
                    setOpacity(0.3);
                    paintMinoBlock(g, x + pdr.x, y + pdr.shadowY, block, mc);
                    setOpacity(1);
                    paintMinoBlock(g, x + pdr.x, y + pdr.y, block, mc);
                    setOpacity(pgd.playerLockProgress * 0.75);
                    paintMinoBlock(g, x + pdr.x, y + pdr.y, block, MinoColor.Gray);
                }
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
