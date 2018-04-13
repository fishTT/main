package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.Theme;
import seedu.address.commons.core.WindowSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private WindowSettings windowSettings;
    private String bookShelfFilePath = "data/bookshelf.xml";
    private String aliasListFilePath = "data/aliaslist.xml";
    private String bookShelfName = "MyBookShelf";
    private Theme appTheme = Theme.DEFAULT_THEME;

    public UserPrefs() {
        windowSettings = new WindowSettings();
    }

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

    public String getAliasListFilePath() {
        return aliasListFilePath;
    }

    public void setAliasListFilePath(String aliasListFilePath) {
        this.aliasListFilePath = aliasListFilePath;
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
                && Objects.equals(aliasListFilePath, o.aliasListFilePath)
                && Objects.equals(bookShelfName, o.bookShelfName)
                && Objects.equals(appTheme, o.appTheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowSettings, bookShelfFilePath, aliasListFilePath, bookShelfName, appTheme);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Window Settings : ").append(windowSettings.toString());
        sb.append("\nBook shelf file location : ").append(bookShelfFilePath);
        sb.append("\nAlias list file location: ").append(aliasListFilePath);
        sb.append("\nBookShelf name : ").append(bookShelfName);
        sb.append("\nTheme : ").append(appTheme);
        return sb.toString();
    }

}
