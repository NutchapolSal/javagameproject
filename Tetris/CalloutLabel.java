package Tetris;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.font.TextAttribute;

public class CalloutLabel extends JLabel {
    private static long animDuration = TimeUnit.SECONDS.toNanos(3);
    private long startTime;

    public CalloutLabel() {
        setText(" ");
        startTime = System.nanoTime() - animDuration;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (startTime + animDuration < System.nanoTime()) {
            return;
        }
        long timeSinceStart = System.nanoTime() - startTime;
        double rawAnimProgress = (double) timeSinceStart / animDuration;
        double spacingAnim = 1 - Math.pow(1 - rawAnimProgress, 7);
        double alphaAnim = Math.pow(Math.max(0.0, (rawAnimProgress * 1.75) - 0.75), 3);

        double spacing = -0.2 + (spacingAnim * 0.25);
        double alpha = 1.0 - alphaAnim;

        Map<TextAttribute, Object> attribute = new HashMap<>();
        attribute.put(TextAttribute.TRACKING, spacing);
        this.setFont(this.getFont().deriveFont(attribute));

        // Fade-out
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
    }
}
