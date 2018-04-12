package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.Theme;
import seedu.address.commons.core.WindowSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {
    //@@author fishTT
    private static UserPrefs instance;

    private Aliases aliases;
    //@@author
    private WindowSettings windowSettings;
    private String bookShelfFilePath = "data/bookshelf.xml";
    private String bookShelfName = "MyBookShelf";
    private Theme appTheme = Theme.DEFAULT_THEME;

    //@@author fishTT
    public UserPrefs() {
        if (instance == null) {
            instance = this;
        }

        this.setGuiSettings(500, 500, 0, 0);
    }

    public static UserPrefs getInstance() {
        if (instance == null) {
            return new UserPrefs();
        }
        return instance;
    }

    public Aliases getAliases() {
        if (aliases == null) {
            aliases = new Aliases();
        }
        return aliases;
    }
    //@@author

    public WindowSettings getWindowSettings() {
        return windowSettings == null ? new WindowSettings() : windowSettings;
    }

    public void updateLastUsedGuiSetting(WindowSettings windowSettings) {
        this.windowSettings = windowSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        windowSettings = new WindowSettings(width, height, x, y);
    }

    public String getBookShelfFilePath() {
        return bookShelfFilePath;
    }

    public void setBookShelfFilePath(String bookShelfFilePath) {
        this.bookShelfFilePath = bookShelfFilePath;
    }

    public String getBookShelfName() {
        return bookShelfName;
    }

    public void setBookShelfName(String bookShelfName) {
        this.bookShelfName = bookShelfName;
    }

    public Theme getAppTheme() {
        return appTheme;
    }

    public void setAppTheme(Theme appTheme) {
        this.appTheme = appTheme;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(windowSettings, o.windowSettings)
                && Objects.equals(bookShelfFilePath, o.bookShelfFilePath)
                && Objects.equals(bookShelfName, o.bookShelfName)
                && Objects.equals(appTheme, o.appTheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowSettings, bookShelfFilePath, bookShelfName);
    }

    @Override
    public String toString() {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("Window Settings : ").append(windowSettings.toString());
        sb.append("\nLocal data file location : ").append(bookShelfFilePath);
        sb.append("\nBookShelf name : ").append(bookShelfName);
        sb.append("\nTheme : ").append(appTheme);
        return sb.toString();
    }

}
