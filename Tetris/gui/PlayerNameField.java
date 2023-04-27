package Tetris.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class PlayerNameField extends JTextField {
    private static final String customFontName = "Tw Cen MT";
    private static final int customFontPlusSize = 3;
    private static final boolean customFontAvailable = getCustomFontAvailable();

    private static boolean getCustomFontAvailable() {
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String name : fontFamilyNames) {
            if (name.equals(customFontName)) {
                return true;
            }
        }
        return false;
    }

    private String placeholder;

    public PlayerNameField() {
        Map<TextAttribute, Object> deriveMap = new HashMap<>();
        if (customFontAvailable) {
            deriveMap.put(TextAttribute.FAMILY, customFontName);
        }
        deriveMap.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        deriveMap.put(TextAttribute.SIZE, getFont().getSize() + (customFontAvailable ? customFontPlusSize : 0) + 8);
        this.setFont(getFont().deriveFont(deriveMap));
    }

    @Override
    protected Document createDefaultModel() {
        return new NameDocument();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        var currColor = g2d.getColor();
        g2d.setColor(new Color(currColor.getRed(), currColor.getGreen(), currColor.getBlue(), 63));
        g2d.drawString(placeholder, getInsets().left, g2d.getFontMetrics()
                .getMaxAscent() + getInsets().top);
    }

    static class NameDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            if (str == null || 5 < offs + str.length()) {
                return;
            }
            super.insertString(offs, str.toUpperCase(), a);
        }
    }

}
