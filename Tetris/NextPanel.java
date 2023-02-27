package Tetris;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

public class NextPanel extends JPanel {
    private static final int PREF_W = 80;
    private static final int PREF_H = 40;

    private Mino mino;

    public NextPanel() {
        int chosenMino = (int) Math.floor(Math.random() * 3);
        switch (chosenMino) {
            case 0:
                mino = Tetromino.I();

                break;
            case 1:
                mino = Tetromino.O();

                break;
            case 2:
                mino = Tetromino.J();

                break;
            case 3:
                mino = Tetromino.L();

                break;
            case 4:
                mino = Tetromino.S();

                break;
            case 5:
                mino = Tetromino.Z();

                break;
            case 6:
                mino = Tetromino.T();
                break;
            default:
                break;
        }
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
    protected void paintComponent(Graphics g) {
        if (mino == null) {
            return;
        }

        Image img;
        try {
            img = ImageIO.read(new File(mino.getColor().filename()));
        } catch (IOException e) {
            e.printStackTrace();
            g.setColor(Color.RED);
            g.fillRect(0, 0, PREF_W, PREF_H);
            g.setColor(Color.WHITE);
            g.drawString("Error", 2, 10);
            return;
        }

        int centerOffsetX = (PREF_W - mino.getShapeWidth() * 20) / 2;
        int centerOffsetY = (PREF_H - mino.getShapeHeight() * 20) / 2;

        for (int y = 0; y < mino.getShapeHeight(); y++) {
            int graphicsY = (1 - y) * 20 - centerOffsetY;
            for (int x = 0; x < mino.getShapeWidth(); x++) {
                int graphicsX = x * 20 + centerOffsetX;
                if (mino.getShapeAtPos(x, y)) {
                    g.drawImage(img, graphicsX, graphicsY, 20, 20, null);
                }
            }
        }

    }

    public void setMino(Mino mino) {
        this.mino = mino;
    }
}
