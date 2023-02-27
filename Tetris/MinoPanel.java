package Tetris;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

public abstract class MinoPanel extends JPanel {
    protected static final int BLOCK_WIDTH = 20;
    protected static final int BLOCK_HEIGHT = 20;
    protected final int PANEL_WIDTH_BLOCKS;
    protected final int PANEL_HEIGHT_BLOCKS;
    protected final int PREF_W;
    protected final int PREF_H;

    protected int centerOffsetX = 0;
    protected int centerOffsetY = 0;

    public MinoPanel(int w, int h) {
        PANEL_WIDTH_BLOCKS = w;
        PANEL_HEIGHT_BLOCKS = h;
        PREF_W = PANEL_WIDTH_BLOCKS * BLOCK_WIDTH;
        PREF_H = PANEL_HEIGHT_BLOCKS * BLOCK_HEIGHT;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    @Override
    abstract protected void paintComponent(Graphics g);

    protected void setMinoCanvasSize(int w, int h) {
        centerOffsetX = (PREF_W - w * BLOCK_WIDTH) / 2;
        centerOffsetY = (PREF_H - h * BLOCK_HEIGHT) / 2;
    }

    protected void resetMinoCanvasSize() {
        centerOffsetX = 0;
        centerOffsetY = 0;
    }

    protected void paintMinoBlock(Graphics g, int x, int y, MinoColor mc) {
        int graphicsX = x * 20 + centerOffsetX;
        int graphicsY = (1 - y) * 20 - centerOffsetY;
        Image img;
        try {
            img = ImageIO.read(new File(mc.filename()));
        } catch (IOException e) {
            e.printStackTrace();
            g.setColor(Color.RED);
            g.fillRect(graphicsX, graphicsY, BLOCK_WIDTH, BLOCK_HEIGHT);
            g.setColor(Color.WHITE);
            g.drawString("Error", graphicsX, graphicsY + BLOCK_HEIGHT);
            return;
        }

        g.drawImage(img, graphicsX, graphicsY, BLOCK_WIDTH, BLOCK_HEIGHT, null);
    }
}
