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
    private Color transparent = new Color(255, 255, 255, 0);
    private Color stripeColor = Color.red;
    private int borderSize = 5;
    private int stripeSize = 10;
    private float stripeStopPos = 0.5f;

    private boolean active = false;
    private double currBorderSize = 0;
    private float currStripeStopPos = 0.5f;

    private long lastFrame = System.nanoTime();
    private float animValue = 0;
    private long animLength = TimeUnit.MILLISECONDS.toNanos(1000);

    public DangerBorder(int fullBorderSize) {
        this.borderSize = fullBorderSize;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        int targetBorderSize = active ? borderSize : 0;
        float targetStripeStopPos = active ? stripeStopPos : 1f;

        long currFrame = System.nanoTime();
        float deltaAnimValue = (float) (currFrame - lastFrame) / animLength;
        animValue += deltaAnimValue;
        animValue %= 1;
        lastFrame = currFrame;

        currBorderSize += (targetBorderSize - currBorderSize) * 0.19;
        currStripeStopPos += (targetStripeStopPos - currStripeStopPos) * 0.24;

        if (1f - smallOffset <= currStripeStopPos) {
            return;
        }

        float xOffset = (animValue * stripeSize * 2)
                + (stripeSize * (1 - currStripeStopPos));

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(
                new LinearGradientPaint(
                        xOffset,
                        0,
                        stripeSize
                                + xOffset,
                        stripeSize,
                        new float[] { currStripeStopPos - smallOffset, currStripeStopPos, 1f -
                                smallOffset, 1f },
                        new Color[] { transparent, stripeColor, stripeColor, transparent },
                        CycleMethod.REPEAT));
        g2d.fillRect(0, 0, width, (int) currBorderSize);
        g2d.fillRect(0, height - (int) currBorderSize, width, (int) currBorderSize);
        g2d.fillRect(0, 0, (int) currBorderSize, height);
        g2d.fillRect(width - (int) currBorderSize, 0, (int) currBorderSize, height);
        g2d.dispose();
    }

    public Insets getBorderInsets(Component c) {
        return new Insets((int) currBorderSize, (int) currBorderSize, (int) currBorderSize, (int) currBorderSize);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void transition(boolean in) {
        active = in;
    }

}
