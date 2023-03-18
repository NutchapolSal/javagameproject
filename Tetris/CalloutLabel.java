package Tetris;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.font.TextAttribute;

public class CalloutLabel extends JLabel {
    private static final String customFontName = "Tw Cen MT";
    private static final int customFontPlusSize = 3;
    private static final boolean customFontAvailable = getCustomFontAvailable();
    private static long animDuration = TimeUnit.SECONDS.toNanos(3);

    private static boolean getCustomFontAvailable() {
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String name : fontFamilyNames) {
            if (name.equals(customFontName)) {
                return true;
            }
        }
        return false;
    }

    private long startTime;
    private boolean isFadeOut = true;

    public CalloutLabel() {
        setText(" ");
        startTime = System.nanoTime() - animDuration;

        if (customFontAvailable) {
            Map<java.awt.font.TextAttribute, Object> deriveMap = new java.util.HashMap<>();
            deriveMap.put(java.awt.font.TextAttribute.FAMILY, customFontName);
            deriveMap.put(java.awt.font.TextAttribute.SIZE, getFont().getSize() + customFontPlusSize);
            this.setFont(getFont().deriveFont(deriveMap));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

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
        double alpha = isFadeOut ? Math.max(0, 1.0 - alphaAnim) : 1;

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
