package systemtests;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalBooks.ARTEMIS;
import static seedu.address.testutil.TypicalBooks.BABYLON_ASHES;
import static seedu.address.testutil.TypicalBooks.COLLAPSING_EMPIRE;
import static seedu.address.testutil.TypicalBooks.CONSIDER_PHLEBAS;
import static seedu.address.testutil.TypicalBooks.WAKING_GODS;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.model.Model;
import seedu.address.model.book.Book;

//@@author takuyakanbr
public class ListCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void list() {
        /* ----------------------------------- Perform valid list operations ---------------------------------------- */

        /* Case: valid filters mode -> 1 book listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " t/artemis a/andy weir c/fiction s/read p/low r/5", ARTEMIS);

        /* Case: valid filters mode -> 0 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " c/space s/reading r/3");

        executeCommand(RecentCommand.COMMAND_WORD);

        /* Case: valid filters and sort mode -> 2 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD + " s/u r/-1 by/title", CONSIDER_PHLEBAS, WAKING_GODS);

        /* Case: no parameters -> all 5 books listed */
        assertListSuccess(ListCommand.COMMAND_WORD,
                BABYLON_ASHES, COLLAPSING_EMPIRE, CONSIDER_PHLEBAS, WAKING_GODS, ARTEMIS);

        /* ----------------------------------- Perform invalid list operations -------------------------------------- */

        /* Case: invalid status filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " s/", Messages.MESSAGE_INVALID_STATUS);

        /* Case: invalid priority and rating filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " p/null r/-100", Messages.MESSAGE_INVALID_PRIORITY);

        /* Case: invalid rating filter -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " r/20", Messages.MESSAGE_INVALID_RATING);

        /* Case: invalid sort mode -> rejected */
        assertCommandFailure(ListCommand.COMMAND_WORD + " by/123", Messages.MESSAGE_INVALID_SORT_BY);
    }

    //@@author
    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing list command with the expected
     * number of listed books.<br>
     * 4. {@code Model}, {@code Storage}, {@code SearchResultsPanel}, and {@code RecentBooksPanel}
     * remain unchanged.<br>
     * 5. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertListSuccess(String command, Book... expectedBooks) {
        List<Book> expectedBookList = Arrays.asList(expectedBooks);
        Model expectedModel = getModel();

        executeCommand(command);

        expectedModel.updateBookListFilter(expectedBookList::contains);
        expectedModel.updateBookListSorter(getModel().getBookListSorter());
        String expectedResultMessage = String.format(ListCommand.MESSAGE_SUCCESS, expectedBooks.length);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertEquals(expectedBooks.length, getModel().getDisplayBookList().size());

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
        assertSelectedBookListCardDeselected();
        assertSelectedSearchResultsCardDeselected();
        assertSelectedRecentBooksCardDeselected();

    }
}
