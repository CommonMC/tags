package org.commonmc.tags;

import java.util.Locale;

/**
 * Vanilla dye colors.
 */
public enum DyeColor {
    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    public String lowercaseName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String formatEntry(String pattern) {
        if (!pattern.contains("{color}")) {
            throw new IllegalArgumentException("Pattern must contain {color} placeholder");
        }

        return pattern.replace("{color}", lowercaseName());
    }
}
