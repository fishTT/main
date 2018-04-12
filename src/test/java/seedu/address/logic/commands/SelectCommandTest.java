package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.showBookAtIndex;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_BOOK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_BOOK;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.BaseEvent;
import seedu.address.commons.events.ui.JumpToBookListIndexRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.BookShelf;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.TypicalBooks;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class SelectCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredBookList_success() {
        Index lastBookIndex = Index.fromOneBased(model.getDisplayBookList().size());

        assertExecutionSuccess(INDEX_FIRST_BOOK);
        assertExecutionSuccess(INDEX_THIRD_BOOK);
        assertExecutionSuccess(lastBookIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredBookList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getDisplayBookList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredBookList_success() {
        showBookAtIndex(model, INDEX_FIRST_BOOK);

        assertExecutionSuccess(INDEX_FIRST_BOOK);
    }

    @Test
    public void execute_validIndexSearchResultsList_success() throws Exception {
        model = new ModelManager();
        model.setActiveListType(ActiveListType.SEARCH_RESULTS);
        BookShelf bookShelf = new BookShelf();
        bookShelf.addBook(TypicalBooks.ARTEMIS);
        bookShelf.addBook(TypicalBooks.BABYLON_ASHES);
        model.updateSearchResults(bookShelf);
        assertExecutionSuccess(INDEX_FIRST_BOOK);
        assertExecutionSuccess(INDEX_SECOND_BOOK);
    }

    @Test
    public void execute_validIndexRecentBooksList_success() throws Exception {
        model = new ModelManager();
        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        model.addRecentBook(TypicalBooks.ARTEMIS);
        model.addRecentBook(TypicalBooks.BABYLON_ASHES);
        assertExecutionSuccess(INDEX_FIRST_BOOK);
        assertExecutionSuccess(INDEX_SECOND_BOOK);
    }

    @Test
    public void execute_invalidIndexFilteredBookList_failure() {
        showBookAtIndex(model, INDEX_FIRST_BOOK);

        Index outOfBoundsIndex = INDEX_SECOND_BOOK;
        // ensures that outOfBoundIndex is still in bounds of book shelf list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getBookShelf().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSearchResultsList_failure() throws Exception {
        model = new ModelManager();
        model.setActiveListType(ActiveListType.SEARCH_RESULTS);
        BookShelf bookShelf = new BookShelf();
        bookShelf.addBook(TypicalBooks.ARTEMIS);
        model.updateSearchResults(bookShelf);
        assertExecutionFailure(INDEX_SECOND_BOOK, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexRecentBooksList_failure() throws Exception {
        model = new ModelManager();
        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        model.addRecentBook(TypicalBooks.ARTEMIS);
        assertExecutionFailure(INDEX_SECOND_BOOK, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(INDEX_FIRST_BOOK);
        SelectCommand selectSecondCommand = new SelectCommand(INDEX_SECOND_BOOK);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCommand selectFirstCommandCopy = new SelectCommand(INDEX_FIRST_BOOK);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different book -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectCommand selectCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectCommand.execute();
            assertEquals(String.format(SelectCommand.MESSAGE_SELECT_BOOK_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        BaseEvent lastEvent = eventsCollectorRule.eventsCollector.getMostRecent();
        int targetIndex = ((JumpToBookListIndexRequestEvent) lastEvent).targetIndex;
        assertEquals(index, Index.fromZeroBased(targetIndex));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectCommand selectCommand = prepareCommand(index);

        try {
            selectCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectCommand} with parameters {@code index}.
     */
    private SelectCommand prepareCommand(Index index) {
        SelectCommand selectCommand = new SelectCommand(index);
        selectCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return selectCommand;
    }
}
