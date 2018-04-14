package systemtests;

import org.junit.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RecentCommand;
import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;

//@@author qiu-siqi
public class RecentCommandSystemTest extends BibliotekSystemTest {

    @Test
    public void recent() {
        /* -------------------------------- Perform valid recent operations ------------------------------------- */

        Model expectedModel = getModel();
        expectedModel.setActiveListType(ActiveListType.RECENT_BOOKS);

        /* Case: Empty recent books list -> 0 book shown */
        assertRecentCommandSuccess(expectedModel);

        /* Case: New selection from book shelf -> 1 book in recent books list */
        executeCommand(ListCommand.COMMAND_WORD);
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        expectedModel.addRecentBook(getModel().getDisplayBookList().get(0));
        assertRecentCommandSuccess(expectedModel);

        /* Case: New selection from search results -> 2 books in recent books list */
        executeBackgroundCommand(SearchCommand.COMMAND_WORD + " Harry", SearchCommand.MESSAGE_SEARCHING);
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        expectedModel = getModel();
        expectedModel.addRecentBook(expectedModel.getSearchResultsList().get(0));
        expectedModel.setActiveListType(ActiveListType.RECENT_BOOKS);
        assertRecentCommandSuccess(expectedModel);

        /* Case: Selecting same book again -> selected book goes to first index of list */
        executeCommand(ListCommand.COMMAND_WORD);
        executeCommand(SelectCommand.COMMAND_WORD + " 1");
        expectedModel.addRecentBook(getModel().getDisplayBookList().get(0));
        assertRecentCommandSuccess(expectedModel);

        /* Case: Selecting a book in recent book list should not change the list */
        executeCommand(SelectCommand.COMMAND_WORD + " 2");
        assertRecentCommandSuccess(expectedModel);

    }

    /**
     * Executes the recent command and verifies that,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the expected message.<br>
     * 4. {@code Model}, {@code Storage} remain unchanged.<br>
     * 5. Any selection in {@code BookListPanel} is deselected.<br>
     * 6. {@code WelcomePanel} is visible.<br>
     * 7. Status bar remains unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see BibliotekSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertRecentCommandSuccess(Model expectedModel) {
        executeCommand(RecentCommand.COMMAND_WORD);
        assertCommandBoxShowsDefaultStyle();

        assertApplicationDisplaysExpected("",
                String.format(RecentCommand.MESSAGE_SUCCESS, expectedModel.getRecentBooksList().size()), expectedModel);

        assertSelectedBookListCardDeselected();
        assertWelcomePanelVisible();
        assertStatusBarUnchanged();
    }

}
