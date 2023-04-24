package Tetris.gui;

import Tetris.data.BlockWithConnection;
import Tetris.data.ObjectDataGrid;
import Tetris.data.PlayerRenderData;
import Tetris.data.easer.EasingFunctions;
import Tetris.data.easer.IntEaser;
import Tetris.data.mino.MinoColor;
import Tetris.gameplay.ClearLinesResult;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.concurrent.TimeUnit;
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
    // private long clearLineStartTime;
    private ClearLinesResult clearLinesResult;

    private IntEaser easerA;

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

        long currTime = System.nanoTime();
        easerA = new IntEaser(currTime);
        easerA.setValueA(0);
        easerA.setValueB(PREF_W);
        easerA.setTimeLength(750, TimeUnit.MILLISECONDS);
        easerA.setEaseFunction(EasingFunctions.easeInOutPower(2));
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

        paintPlayer(g);
        paintClearLineAnimation(g);
    }

    private void paintPlayer(Graphics g) {
        if (pdr == null) {
            return;
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
                setOpacity(playerLockProgress * 0.75);
                paintMinoBlock(g, x + pdr.x, y + pdr.y, block, MinoColor.Gray);
            }
        }
    }

    private void paintClearLineAnimation(Graphics gIn) {
        Graphics2D g = (Graphics2D) gIn.create();
        if (clearLinesResult == null || clearLinesResult.count == 0) {
            return;
        }

        long currTime = System.nanoTime();
        int xPos = easerA.getValue(currTime);
        g.setPaint(new LinearGradientPaint(0, 0, 20, 20, new float[] { 0.499f, 0.5f, 0.999f, 1f },
                new Color[] { new Color(255, 255, 255, 0), Color.white, Color.white, new Color(255, 255, 255, 0) },
                CycleMethod.REPEAT));
        for (int v : clearLinesResult.lineIndices) {
            int yPos = (PANEL_HEIGHT_BLOCKS - 1 - v) * BLOCK_HEIGHT;
            g.fillRect(xPos, yPos, PREF_W - xPos, BLOCK_HEIGHT);
        }
    }

    public void startAnimation(String s, boolean isFadeOut) {
        callout.startAnimation(s, isFadeOut);
    }

    public void startAnimation(String s) {
        callout.startAnimation(s);
    }

    public void startClearLineAnimation(ClearLinesResult clr) {
        // clearLineStartTime = System.nanoTime();
        this.clearLinesResult = clr;
        easerA.startEase(System.nanoTime(), true);
    }
}
