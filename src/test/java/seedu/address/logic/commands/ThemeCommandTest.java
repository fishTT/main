package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Theme;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.network.NetworkManager;
import seedu.address.ui.testutil.EventsCollectorRule;

public class ThemeCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void execute_validTheme_success() {
        assertExecutionSuccess(Theme.WHITE);
        assertExecutionSuccess(Theme.LIGHT);
        assertExecutionSuccess(Theme.DARK);
    }

    @Test
    public void equals() {
        final ThemeCommand standardCommand = prepareCommand(Theme.WHITE);

        // same values -> returns true
        ThemeCommand commandWithSameValues = prepareCommand(Theme.WHITE);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different themes -> returns false
        assertFalse(standardCommand.equals(new ThemeCommand(Theme.DARK)));
    }

    /**
     * Executes a {@code ThemeCommand} with the given {@code theme}, and checks that
     * {@code ChangeThemeRequestEvent} is raised with the correct parameters.
     */
    private void assertExecutionSuccess(Theme theme) {
        ThemeCommand command = prepareCommand(theme);
        Model expectedModel = new ModelManager(new BookShelf(model.getBookShelf()), new UserPrefs());
        assertCommandSuccess(command, model,
                String.format(ThemeCommand.MESSAGE_SUCCESS, theme.getThemeName()), expectedModel);

        ChangeThemeRequestEvent lastEvent =
                (ChangeThemeRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(theme, lastEvent.newTheme);
    }

    private ThemeCommand prepareCommand(Theme theme) {
        ThemeCommand themeCommand = new ThemeCommand(theme);
        themeCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return themeCommand;
    }
}
