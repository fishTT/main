package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void clear() {
        Model model = getModel();

        /* Case: clear non-empty book shelf, command with leading spaces and trailing alphanumeric characters and
         * spaces -> cleared */
        assertCommandSuccess("   " + ClearCommand.COMMAND_WORD + " ab12   ");
        assertSelectedBookListCardUnchanged();

        /* Case: undo clearing book shelf -> original book shelf restored */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = ClearCommand.UNDO_SUCCESS;
        assertCommandSuccess(command,  expectedResultMessage, model);
        assertSelectedBookListCardUnchanged();

        /* Case: selects first card in book list and clears book shelf -> cleared and no card selected */
        executeCommand(UndoCommand.COMMAND_WORD); // restores the original book shelf
        selectBook(Index.fromOneBased(1));
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedBookListCardDeselected();

        /* Case: clear empty book shelf -> cleared */
        assertCommandSuccess(ClearCommand.COMMAND_WORD);
        assertSelectedBookListCardUnchanged();

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ClEaR", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code ClearCommand#MESSAGE_SUCCESS} and the model related components equal to an empty model.
     * These verifications are done by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the command box has the default style class and the status bar's sync status changes.
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command) {
        Model model = getModel();
        assertCommandSuccess(command, ClearCommand.MESSAGE_SUCCESS, new ModelManager(new BookShelf(), new UserPrefs(),
                model.getRecentBooksListAsBookShelf(), model.getAliasList()));
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except that the result box displays
     * {@code expectedResultMessage} and the model related components equal to {@code expectedModel}.
     * @see ClearCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, String expectedResultMessage, Model expectedModel) {
        executeCommand(command);

        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

}
