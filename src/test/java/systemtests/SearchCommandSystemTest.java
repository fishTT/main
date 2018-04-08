package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.AUTHOR_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.CATEGORY_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.ISBN_DESC_ARTEMIS;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_ARTEMIS;

import org.junit.Test;

import guitests.GuiRobot;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;

//@@author takuyakanbr
public class SearchCommandSystemTest extends BibliotekSystemTest {
    @Test
    public void search() throws Exception {
        /* ----------------------------------- Perform invalid search operations ------------------------------------ */

        /* Case: no search term or parameters -> rejected */
        assertCommandFailure(SearchCommand.COMMAND_WORD, SearchCommand.MESSAGE_EMPTY_QUERY);

        /* Case: no search term or parameters -> rejected */
        assertCommandFailure("   " + SearchCommand.COMMAND_WORD + "             ", SearchCommand.MESSAGE_EMPTY_QUERY);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("SeaRcH hello", MESSAGE_UNKNOWN_COMMAND);

        /* Case: misspelled command word -> rejected */
        assertCommandFailure("searchh hello", MESSAGE_UNKNOWN_COMMAND);

        /* ----------------------------------- Perform valid search operations -------------------------------------- */

        // Note: these tests require network connection.

        /* Case: search for books given search term -> success */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + " hello");

        /* Case: search for books given search parameters -> success */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + AUTHOR_DESC_ARTEMIS);

        /* Case: search for non-existant book -> return 0 results */
        assertSearchSuccess(SearchCommand.COMMAND_WORD + TITLE_DESC_ARTEMIS + CATEGORY_DESC_ARTEMIS
                + ISBN_DESC_ARTEMIS + AUTHOR_DESC_ARTEMIS);

        /* ----------------------------------- Perform commands on search results ----------------------------------- */

        /* Case: trying to clear or delete result items -> rejected */
        assertCommandFailure(ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_WRONG_ACTIVE_LIST);
        assertCommandFailure(DeleteCommand.COMMAND_WORD + " 1", DeleteCommand.MESSAGE_WRONG_ACTIVE_LIST);
    }

    //@@author
    /**
     * Executes {@code command} and verifies that, after the web API has returned a result,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the search successful message.<br>
     * 4. {@code Storage} and{@code BookListPanel} remain unchanged.<br>
     * 5. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see BibliotekSystemTest#assertSelectedBookListCardChanged(Index)
     */
    private void assertSearchSuccess(String command) throws Exception {
        Model expectedModel = getModel();

        executeCommand(command);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();

        new GuiRobot().waitForEvent(() -> !getResultDisplay().getText().equals(SearchCommand.MESSAGE_SEARCHING),
                GuiRobot.NETWORK_ACTION_TIMEOUT_MILLISECONDS);
        new GuiRobot().waitForEvent(() -> getCommandBox().isEnabled(), GuiRobot.NETWORK_ACTION_TIMEOUT_MILLISECONDS);

        BookShelf searchResults = new BookShelf();
        searchResults.setBooks(getModel().getSearchResultsList());
        expectedModel.updateSearchResults(searchResults);

        assertApplicationDisplaysExpected("",
                String.format(SearchCommand.MESSAGE_SEARCH_SUCCESS, searchResults.size()), expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertCommandBoxEnabled();
        assertStatusBarUnchanged();
    }

}
