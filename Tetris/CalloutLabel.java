package Tetris;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.font.TextAttribute;

public class CalloutLabel extends JLabel {
    private static long animDuration = TimeUnit.SECONDS.toNanos(2);
    private long startTime;
    private boolean noPaint;

    public CalloutLabel() {
        setText(" ");
        startTime = System.nanoTime() - animDuration;
        noPaint = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (noPaint) {
            return;
        }
        long timeSinceStart = System.nanoTime() - startTime;
        double rawAnimProgress = (double) timeSinceStart / animDuration;
        double animationProgress = 1 - Math.pow(1 - rawAnimProgress, 5);

        double spacing = -0.1 + (animationProgress * 0.24);
        double alpha = Math.min(1.0, 2.0 - rawAnimProgress * 2);

        if (animationProgress >= 1) {
            noPaint = true;
            return;
        }
        // spacing += (spacing * animationProgress);
        Map<TextAttribute, Object> attribute = new HashMap<>();
        attribute.put(TextAttribute.TRACKING, spacing);
        // // spacing += (0.1 - spacing) / 30;
        this.setFont(this.getFont().deriveFont(attribute));

        // Fade-out
        // alpha -= (alpha * animationProgress);
        if (alpha < 0) {
            alpha = 0;
        }
        this.setForeground(new Color(this.getForeground().getRed(), this.getForeground().getGreen(),
                this.getForeground().getBlue(), (int) (255 * alpha)));
        super.paintComponent(g);

    }

    public void startAnimation(String s) {
        startTime = System.nanoTime();
        this.setText(s);
        noPaint = false;
    }
}
