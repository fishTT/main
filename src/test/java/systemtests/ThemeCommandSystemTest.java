package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import seedu.address.commons.core.Theme;
import seedu.address.logic.commands.ThemeCommand;
import seedu.address.model.Model;

//@@author takuyakanbr
public class ThemeCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void theme() {
        /* ----------------------------------- Perform invalid theme operations ------------------------------------- */

        /* Case: no theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ThemeCommand.MESSAGE_USAGE));

        /* Case: invalid theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD + " 12345",
                String.format(ThemeCommand.MESSAGE_INVALID_THEME, "12345"));

        /* Case: invalid theme name -> rejected */
        assertCommandFailure(ThemeCommand.COMMAND_WORD + " light 123",
                String.format(ThemeCommand.MESSAGE_INVALID_THEME, "light 123"));

        /* ----------------------------------- Perform valid theme operations --------------------------------------- */

        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " light", Theme.LIGHT);
        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " DARK", Theme.DARK);
        assertThemeCommandSuccess(ThemeCommand.COMMAND_WORD + " WhitE", Theme.WHITE);
    }

    //@@author
    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ThemeCommand#MESSAGE_SUCCESS} and the model related components remain unchanged.
     * These verifications are done by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class, the status bar's sync status does not
     * change, and the active stylesheet has changed to the one that is specified by the {@code theme}.
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertThemeCommandSuccess(String command, Theme theme) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected("",
                String.format(ThemeCommand.MESSAGE_SUCCESS, theme.getThemeName()), expectedModel);
        assertSelectedBookListCardUnchanged();
        assertSelectedSearchResultsCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
        assertEquals(theme.getCssFile(), getMainWindowHandle().getActiveStylesheet());
    }

}
