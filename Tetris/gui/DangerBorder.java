package Tetris.gui;

import Tetris.data.easer.EasingFunctions;
import Tetris.data.easer.FloatEaser;
import Tetris.data.easer.IntEaser;
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
    private int stripeSize = 10;
    private float stripeStopPos = 0.5f;

    private long lastFrame = System.nanoTime();
    private float animValue = 0;
    private long loopLength = TimeUnit.MILLISECONDS.toNanos(1000);

    private IntEaser easedBorderSize = new IntEaser(lastFrame);
    private FloatEaser easedStripeStopPos = new FloatEaser(lastFrame);
    private int currBorderSize = 0;

    public DangerBorder(int fullBorderSize) {
        easedBorderSize.setValueA(0);
        easedBorderSize.setValueB(fullBorderSize);
        easedBorderSize.setTimeLength(200, TimeUnit.MILLISECONDS);
        easedBorderSize.setEaseFunction(EasingFunctions.easeOutPower(3));
        easedBorderSize.setTimeLengthBToA(800, TimeUnit.MILLISECONDS);
        easedBorderSize.setEaseBToAFunction(EasingFunctions.easeInExpo());
        easedStripeStopPos.setValueA(1);
        easedStripeStopPos.setValueB(stripeStopPos);
        easedStripeStopPos.setTimeLength(500, TimeUnit.MILLISECONDS);
        easedStripeStopPos.setEaseFunction(EasingFunctions.easeInPower(5));
        easedStripeStopPos.setTimeLengthBToA(1500, TimeUnit.MILLISECONDS);
        easedStripeStopPos.setEaseBToAFunction(EasingFunctions.easeOutExpo());
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        long currFrame = System.nanoTime();
        float deltaAnimValue = (float) (currFrame - lastFrame) / loopLength;
        animValue += deltaAnimValue;
        animValue %= 1;
        lastFrame = currFrame;

        currBorderSize = easedBorderSize.getValue(currFrame);
        float currStripeStopPos = easedStripeStopPos.getValue(currFrame);

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
        g2d.fillRect(0, 0, width, currBorderSize);
        g2d.fillRect(0, height - currBorderSize, width, currBorderSize);
        g2d.fillRect(0, 0, currBorderSize, height);
        g2d.fillRect(width - currBorderSize, 0, currBorderSize, height);
        g2d.dispose();
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(currBorderSize, currBorderSize, currBorderSize, currBorderSize);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void transition(boolean in) {
        long currTime = System.nanoTime();
        easedStripeStopPos.setValueA(in ? smallOffset : 1);
        easedBorderSize.startEase(currTime, in);
        easedStripeStopPos.startEase(currTime, in);
    }

}
