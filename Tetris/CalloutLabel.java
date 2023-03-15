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
    private boolean isFadeOut = true;

    public CalloutLabel() {
        setText(" ");
        startTime = System.nanoTime() - animDuration;

        Map<java.awt.font.TextAttribute, Object> deriveMap = new java.util.HashMap<>();
        deriveMap.put(java.awt.font.TextAttribute.FAMILY, "Tw Cen MT");
        deriveMap.put(java.awt.font.TextAttribute.SIZE, getFont().getSize() + 3);
        this.setFont(getFont().deriveFont(deriveMap));
    }

    @Override
    protected void paintComponent(Graphics g) {
        long timeSinceStart = System.nanoTime() - startTime;
        double rawAnimProgress = (double) timeSinceStart / animDuration;
        rawAnimProgress = Math.min(1, rawAnimProgress);
        if (rawAnimProgress == 1) {
            if (!isFadeOut) {
                super.paintComponent(g);
            }
            return;
        }
        double spacingAnim = 1 - Math.pow(1 - rawAnimProgress, 7);
        double alphaAnim = Math.pow(Math.max(0.0, (rawAnimProgress * 1.75) - 0.75), 3);

        double spacing = -0.2 + (spacingAnim * 0.25);
        double alpha = isFadeOut ? 1.0 - alphaAnim : 1;

        Map<TextAttribute, Object> attribute = new HashMap<>();
        attribute.put(TextAttribute.TRACKING, spacing);
        this.setFont(this.getFont().deriveFont(attribute));

        this.setForeground(new Color(this.getForeground().getRed(), this.getForeground().getGreen(),
                this.getForeground().getBlue(), (int) (255 * alpha)));

        super.paintComponent(g);

    }

    public void startAnimation(String s, boolean isFadeOut) {
        startTime = System.nanoTime();
        this.isFadeOut = isFadeOut;
        this.setText(s);
    }

    public void startAnimation(String s) {
        startAnimation(s, true);
    }

}
