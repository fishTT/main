package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.SelectCommand.MESSAGE_SELECT_BOOK_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.testutil.TypicalBooks;

public class SelectCommandSystemTest extends BibliotekSystemTest {
    @Test
    public void select() {
        /* ------------------------ Perform select operations on the shown unfiltered list -------------------------- */

        /* Case: select the first card in the book list, command with leading spaces and trailing spaces
         * -> selected
         */
        String command = "   " + SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_BOOK.getOneBased() + "   ";
        assertBookListSelectSuccess(command, INDEX_FIRST_BOOK);

        /* Case: select the last card in the book list -> selected */
        Index bookCount = Index.fromOneBased(TypicalBooks.getTypicalBooks().size());
        command = SelectCommand.COMMAND_WORD + " " + bookCount.getOneBased();
        assertBookListSelectSuccess(command, bookCount);

        /* Case: undo previous selection -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: select the middle card in the book list -> selected */
        Index middleIndex = Index.fromOneBased(bookCount.getOneBased() / 2);
        command = SelectCommand.COMMAND_WORD + " " + middleIndex.getOneBased();
        assertBookListSelectSuccess(command, middleIndex);

        /* Case: select the current selected card -> selected */
        assertBookListSelectSuccess(command, middleIndex);

        /* ------------------------ Perform select operations on the shown filtered list ---------------------------- */

        executeCommand(ListCommand.COMMAND_WORD + " s/unread");

        /* Case: select the first card in the displayed book list -> selected */
        command = SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_BOOK.getOneBased();
        assertBookListSelectSuccess(command, INDEX_FIRST_BOOK);

        /* Case: select the last card in the displayed book list -> selected */
        bookCount = Index.fromOneBased(getModel().getDisplayBookList().size());
        command = SelectCommand.COMMAND_WORD + " " + bookCount.getOneBased();
        assertBookListSelectSuccess(command, bookCount);

        /* ----------------------------------- Perform invalid select operations ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + 0,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + -1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        int invalidIndex = getModel().getDisplayBookList().size() + 1;
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex, MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(SelectCommand.COMMAND_WORD + " 1 abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeLeCt 1", MESSAGE_UNKNOWN_COMMAND);

        /* Case: select from empty book shelf -> rejected */
        deleteAllBooks();
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_BOOK.getOneBased(),
                MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);

        /* ------------------------ Perform select operations on the shown results list ----------------------------- */

        // Note: this test requires an Internet connection.
        executeBackgroundCommand(SearchCommand.COMMAND_WORD + " hello", SearchCommand.MESSAGE_SEARCHING);
        assertSearchResultsSelectSuccess(SelectCommand.COMMAND_WORD + " 1", Index.fromOneBased(1));
        assertSearchResultsSelectSuccess(SelectCommand.COMMAND_WORD + " 1", Index.fromOneBased(1));

        invalidIndex = getModel().getSearchResultsList().size() + 1;
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex,
                MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);

        /* -------------------- Perform select operations on the shown recent books list ------------------------- */
        executeCommand(RecentCommand.COMMAND_WORD);
        assertRecentBooksSelectSuccess(SelectCommand.COMMAND_WORD + " 1", Index.fromOneBased(1));
        assertRecentBooksSelectSuccess(SelectCommand.COMMAND_WORD + " 1", Index.fromOneBased(1));

        invalidIndex = getModel().getRecentBooksList().size() + 1;
        assertCommandFailure(SelectCommand.COMMAND_WORD + " " + invalidIndex,
                MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected book.<br>
     * 4. {@code Model}, {@code Storage}, {@code BookListPanel}, {@code SearchResultsPanel},
     * and {@code RecentBooksPanel} remain unchanged.<br>
     * 5. Selected book list card is at {@code expectedSelectedCardIndex}.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see BibliotekSystemTest#assertSelectedBookListCardChanged(Index)
     */
    private void assertBookListSelectSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_BOOK_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getBookListPanel().getSelectedCardIndex();
        expectedModel.addRecentBook(expectedModel.getDisplayBookList().get(
                expectedSelectedCardIndex.getZeroBased()));

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedBookListCardUnchanged();
        } else {
            assertSelectedBookListCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected search result.<br>
     * 4. {@code Model}, {@code Storage}, {@code BookListPanel}, {@code SearchResultsPanel},
     * and {@code RecentBooksPanel} remain unchanged.<br>
     * 5. Selected search results card is at {@code expectedSelectedCardIndex}.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see BibliotekSystemTest#assertSelectedSearchResultsCardChanged(Index)
     */
    private void assertSearchResultsSelectSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_BOOK_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getSearchResultsPanel().getSelectedCardIndex();
        expectedModel.addRecentBook(expectedModel.getSearchResultsList().get(
                expectedSelectedCardIndex.getZeroBased()));

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedSearchResultsCardUnchanged();
        } else {
            assertSelectedSearchResultsCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing select command with the
     * {@code expectedSelectedCardIndex} of the selected search result.<br>
     * 4. {@code Model}, {@code Storage}, {@code BookListPanel}, {@code SearchResultsPanel},
     * and {@code RecentBooksPanel} remain unchanged.<br>
     * 5. Selected card is at {@code expectedSelectedCardIndex} and the browser url is updated accordingly.<br>
     * 6. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see BibliotekSystemTest#assertSelectedRecentBooksCardChanged(Index)
     */
    private void assertRecentBooksSelectSuccess(String command, Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        String expectedResultMessage = String.format(
                MESSAGE_SELECT_BOOK_SUCCESS, expectedSelectedCardIndex.getOneBased());
        int preExecutionSelectedCardIndex = getRecentBooksPanel().getSelectedCardIndex();

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (preExecutionSelectedCardIndex == expectedSelectedCardIndex.getZeroBased()) {
            assertSelectedRecentBooksCardUnchanged();
        } else {
            assertSelectedRecentBooksCardChanged(expectedSelectedCardIndex);
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

}
