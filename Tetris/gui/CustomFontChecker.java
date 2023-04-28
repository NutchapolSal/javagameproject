package Tetris.gui;

import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;

public class CustomFontChecker {
    private static final Map<String, CustomFontChecker> checkers = new HashMap<>();
    private static final String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAvailableFontFamilyNames();
    private static final Map<String, Integer> plusSizes = new HashMap<>();
    static {
        plusSizes.put("Tw Cen MT", 3);
    }
    public final String fontName;
    public final int plusSize;
    public final boolean available;

    private CustomFontChecker(String fontName) {
        this.fontName = fontName;
        this.plusSize = plusSizes.getOrDefault(fontName, 0);

        boolean isAvailable = false;
        for (String name : fontFamilyNames) {
            if (name.equals(fontName)) {
                isAvailable = true;
                break;
            }
        }

        this.available = isAvailable;

    }

    public static CustomFontChecker getFontChecker(String fontName) {
        if (!checkers.containsKey(fontName)) {
            checkers.put(fontName, new CustomFontChecker(fontName));
        }

        return checkers.get(fontName);
    }
}
