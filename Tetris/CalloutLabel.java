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
        long timeSinceStart = System.nanoTime() - startTime;
        double rawAnimProgress = (double) timeSinceStart / animDuration;
        rawAnimProgress = Math.min(1, rawAnimProgress);
        if (rawAnimProgress == 1) {
            if (!isFadeOut) {
                super.paintComponent(g);
            }
            return;
        }
        double animValue1 = Math.pow(Math.min(rawAnimProgress * 8, 1), 2);
        double animValue2 = 1 - Math.pow(1 - rawAnimProgress, 10);

        double alphaAnim = Math.pow(Math.max(0.0, (rawAnimProgress * 1.75) - 0.75), 3);

        double alpha = isFadeOut ? Math.max(0, 1.0 - alphaAnim) : 1;

        var textG = g.create();

        var clipbounds = g.getClipBounds();
        int xBlockWidth = (int) (clipbounds.width * animValue2);
        int xBlockOrigin = (int) (clipbounds.width * animValue1) + 1;
        int yBlockWidth = (int) (clipbounds.height * animValue2);
        int yBlockOrigin = (int) (clipbounds.height * animValue1) + 1;
        switch (getHorizontalAlignment()) {
            case LEFT:
            case LEADING:
                g.fillRect(clipbounds.x + xBlockOrigin,
                        clipbounds.y,
                        xBlockWidth,
                        clipbounds.height);

                textG.clipRect(clipbounds.x,
                        clipbounds.y,
                        clipbounds.x + xBlockOrigin + xBlockWidth,
                        clipbounds.height);
                break;

            case CENTER:
                g.fillRect(clipbounds.x,
                        clipbounds.y + yBlockOrigin,
                        clipbounds.width,
                        yBlockWidth);

                textG.clipRect(clipbounds.x,
                        clipbounds.y,
                        clipbounds.width,
                        clipbounds.y + yBlockOrigin + yBlockWidth);
                break;

            case RIGHT:
            case TRAILING:
                g.fillRect(clipbounds.x + clipbounds.width - (xBlockWidth + xBlockOrigin),
                        clipbounds.y,
                        xBlockWidth,
                        clipbounds.height);

                textG.clipRect(clipbounds.x + clipbounds.width - (xBlockWidth + xBlockOrigin),
                        clipbounds.y,
                        xBlockWidth + xBlockOrigin,
                        clipbounds.height);
                break;
        }

        this.setForeground(new Color(this.getForeground().getRed(), this.getForeground().getGreen(),
                this.getForeground().getBlue(), (int) (255 * alpha)));

        super.paintComponent(textG);

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
