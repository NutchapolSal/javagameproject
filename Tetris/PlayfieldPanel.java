package Tetris;

import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;

public class PlayfieldPanel extends JPanel {
    private static final int PREF_W = 200;
    private static final int PREF_H = 400;

    public PlayfieldPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        g.drawString("Playfield", 100, 200);
    }
}
