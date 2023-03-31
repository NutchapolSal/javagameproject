package Tetris.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.concurrent.TimeUnit;
import javax.swing.border.Border;

public class DangerBorder implements Border {
    private static float smallOffset = 0.01f;
    private double borderSize = 0;
    private int targetBorderSize = 0;
    private int fullBorderSize = 5;
    private int stripeSize = 10;
    private float stripeOffset = 0.5f;
    private Color colorA = Color.white;
    private Color colorB = Color.red;
    private double animValue = 0;
    private long lastFrame = System.nanoTime();
    private long animLength = TimeUnit.MILLISECONDS.toNanos(1000);

    public DangerBorder(int fullBorderSize) {
        this.fullBorderSize = fullBorderSize;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        long currFrame = System.nanoTime();
        double deltaAnimValue = (double) (currFrame - lastFrame) / animLength;
        animValue += deltaAnimValue;
        animValue %= 1;
        lastFrame = currFrame;

        borderSize += (targetBorderSize - borderSize) * 0.2;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(
                new LinearGradientPaint((int) (animValue * stripeSize * 2), 0,
                        stripeSize + (int) (animValue * stripeSize * 2),
                        stripeSize,
                        new float[] { stripeOffset - smallOffset, stripeOffset, 1f - smallOffset, 1f },
                        new Color[] { colorA, colorB, colorB, colorA },
                        CycleMethod.REPEAT));
        g2d.fillRect(0, 0, width, (int) borderSize);
        g2d.fillRect(0, height - (int) borderSize, width, (int) borderSize);
        g2d.fillRect(0, 0, (int) borderSize, height);
        g2d.fillRect(width - (int) borderSize, 0, (int) borderSize, height);
        g2d.dispose();
    }

    public Insets getBorderInsets(Component c) {
        return new Insets((int) borderSize, (int) borderSize, (int) borderSize, (int) borderSize);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void transition(boolean in) {
        targetBorderSize = in ? fullBorderSize : 0;
    }

}
