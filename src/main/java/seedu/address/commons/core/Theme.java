package seedu.address.commons.core;

import seedu.address.commons.exceptions.InvalidThemeException;

//@@author takuyakanbr
/**
 * Represents an application theme.
 */
public enum Theme {
    WHITE("view/WhiteTheme.css"),
    LIGHT("view/LightTheme.css"),
    DARK("view/DarkTheme.css");

    public static final Theme DEFAULT_THEME = WHITE;

    private final String cssFile;

    Theme(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getThemeName() {
        return name().toLowerCase();
    }

    public String getCssFile() {
        return cssFile;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Returns the theme with a theme name that matches the specified {@code themeName}.
     *
     * @throws InvalidThemeException if no matching theme can be found.
     */
    public static Theme getThemeByName(String themeName) throws InvalidThemeException {
        for (Theme theme : values()) {
            if (themeName.equalsIgnoreCase(theme.getThemeName())) {
                return theme;
            }
        }

        throw new InvalidThemeException("Invalid theme name: " + themeName);
    }

}
