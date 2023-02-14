package Tetris;

import java.awt.Graphics;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

public class MinoPanel extends JPanel {
    private static final int PREF_W = 80;
    private static final int PREF_H = 40;

    public MinoPanel() {
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
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, PREF_W, PREF_H);
        g.setColor(Color.WHITE);
        g.drawString("MinoPanel", 20, 10);
    }
}
