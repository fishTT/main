package seedu.address.logic.commands;

import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.TypicalBooks;

//@@author qiu-siqi
/**
 * Contains integration tests (interaction with the Model) and unit tests for RecentCommand.
 */
public class RecentCommandTest {

    private Model model;
    private Model expectedModel;
    private RecentCommand recentCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
        expectedModel = new ModelManager(model.getBookShelf(), new UserPrefs());

        recentCommand = new RecentCommand();
        recentCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
    }

    @Test
    public void execute_showsEmptyRecent() {
        assertCommandSuccess(recentCommand, model, String.format(RecentCommand.MESSAGE_SUCCESS, 0), expectedModel);
    }

    @Test
    public void execute_showsNonEmptyRecent() {
        model.addRecentBook(TypicalBooks.ARTEMIS);
        expectedModel.addRecentBook(TypicalBooks.ARTEMIS);
        assertCommandSuccess(recentCommand, model, String.format(RecentCommand.MESSAGE_SUCCESS, 1), expectedModel);
    }
}
