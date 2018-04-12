package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_BOOK_SUCCESS;
import static seedu.address.testutil.TestUtil.getBook;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.book.Book;
import seedu.address.model.book.exceptions.BookNotFoundException;

public class DeleteCommandSystemTest extends BibliotekSystemTest {

    private static final String MESSAGE_INVALID_DELETE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);

    @Test
    public void delete() {
        /* ----------------- Performing delete operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first book in the list, command with leading spaces and trailing spaces -> deleted */
        Model expectedModel = getModel();
        String command = "     " + DeleteCommand.COMMAND_WORD + "      " + INDEX_FIRST_BOOK.getOneBased() + "       ";
        Book deletedBook = removeBook(expectedModel, INDEX_FIRST_BOOK);
        String expectedResultMessage = String.format(MESSAGE_DELETE_BOOK_SUCCESS, deletedBook);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: delete the last book in the list -> deleted */
        Model modelBeforeDeletingLast = getModel();
        Index lastBookIndex = getLastIndex(modelBeforeDeletingLast);
        deletedBook = getModel().getDisplayBookList().get(lastBookIndex.getZeroBased());
        assertCommandSuccess(lastBookIndex);

        /* Case: undo deleting the last book in the list -> last book restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = String.format(DeleteCommand.UNDO_SUCCESS, deletedBook);
        assertCommandSuccess(command, modelBeforeDeletingLast, expectedResultMessage);

        /* Case: delete the middle book in the list -> deleted */
        Index middleBookIndex = getMidIndex(getModel());
        assertCommandSuccess(middleBookIndex);

        /* --------------------- Performing delete operation while a book card is selected ------------------------ */

        /* Case: delete the selected book -> book list panel selects the book before the deleted book */
        showAllBooks();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectBook(selectedIndex);
        expectedModel.addRecentBook(expectedModel.getDisplayBookList().get(selectedIndex.getZeroBased()));
        command = DeleteCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        deletedBook = removeBook(expectedModel, selectedIndex);
        expectedModel.addRecentBook(expectedModel.getDisplayBookList().get(expectedIndex.getZeroBased()));
        expectedResultMessage = String.format(MESSAGE_DELETE_BOOK_SUCCESS, deletedBook);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getDisplayBookList().size() + 1);
        command = DeleteCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_DELETE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("DelETE 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Book} at the specified {@code index} in {@code model}'s display book list.
     * @return the removed book.
     */
    private Book removeBook(Model model, Index index) {
        Book targetBook = getBook(model, index);
        try {
            model.deleteBook(targetBook);
        } catch (BookNotFoundException pnfe) {
            throw new AssertionError("targetBook is retrieved from model.");
        }
        return targetBook;
    }

    /**
     * Deletes the book at {@code toDelete} by creating a default {@code DeleteCommand} using {@code toDelete} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(Index toDelete) {
        Model expectedModel = getModel();
        Book deletedBook = removeBook(expectedModel, toDelete);
        String expectedResultMessage = String.format(MESSAGE_DELETE_BOOK_SUCCESS, deletedBook);

        assertCommandSuccess(
                DeleteCommand.COMMAND_WORD + " " + toDelete.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the selected book list card and search results card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the selected
     * book list card is expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see BibliotekSystemTest#assertSelectedBookListCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedBookListCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedBookListCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

}
