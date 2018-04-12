package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_BOOK;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalBooks;
import seedu.address.ui.testutil.EventsCollectorRule;

//@@author qiu-siqi
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code LibraryCommand}.
 */
public class LibraryCommandTest {

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    /**
     * Default active list is book shelf.
     */
    @Before
    public void setUp() {
        model = new ModelManager(TypicalBooks.getTypicalBookShelf(), new UserPrefs());
        model.updateSearchResults(TypicalBooks.getTypicalBookShelf());
        model.addRecentBook(TypicalBooks.ARTEMIS);
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new LibraryCommand(null);
    }

    @Test
    public void execute_validIndexBookShelf_success() {
        assertExecutionSuccess(INDEX_FIRST_BOOK, model.getDisplayBookList().get(0), model);
    }

    @Test
    public void execute_invalidIndexBookShelf_failure() {
        LibraryCommand libraryCommand = prepareCommand(Index.fromOneBased(model.getDisplayBookList().size() + 1));

        assertCommandFailure(libraryCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexSearchResults_success() {
        model.setActiveListType(ActiveListType.SEARCH_RESULTS);
        assertExecutionSuccess(INDEX_FIRST_BOOK, model.getSearchResultsList().get(0), model);
    }

    @Test
    public void execute_invalidIndexSearchResults_failure() {
        model.setActiveListType(ActiveListType.SEARCH_RESULTS);
        LibraryCommand libraryCommand = prepareCommand(Index.fromOneBased(model.getSearchResultsList().size() + 1));

        assertCommandFailure(libraryCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexRecentBooks_success() {
        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        assertExecutionSuccess(INDEX_FIRST_BOOK, model.getRecentBooksList().get(0), model);
    }

    @Test
    public void execute_invalidIndexRecentBooks_failure() {
        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        LibraryCommand libraryCommand = prepareCommand(Index.fromOneBased(model.getRecentBooksList().size() + 1));

        assertCommandFailure(libraryCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_networkError_raisesExpectedEvent() throws CommandException {
        LibraryCommand command = new LibraryCommand(INDEX_FIRST_BOOK);

        NetworkManager networkManagerMock = mock(NetworkManager.class);
        when(networkManagerMock.searchLibraryForBook(
                model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased())))
                .thenReturn(TestUtil.getFailedFuture());

        command.setData(model, networkManagerMock, new CommandHistory(), new UndoStack());
        command.execute();

        NewResultAvailableEvent resultEvent = (NewResultAvailableEvent)
                eventsCollectorRule.eventsCollector.getMostRecent(NewResultAvailableEvent.class);
        assertEquals(LibraryCommand.MESSAGE_FAIL, resultEvent.message);
    }

    @Test
    public void equals() {
        LibraryCommand libraryFirstCommand = prepareCommand(INDEX_FIRST_BOOK);
        LibraryCommand librarySecondCommand = prepareCommand(INDEX_SECOND_BOOK);

        // same object -> returns true
        assertTrue(libraryFirstCommand.equals(libraryFirstCommand));

        // same values -> returns true
        LibraryCommand libraryFirstCommandCopy = prepareCommand(INDEX_FIRST_BOOK);
        assertTrue(libraryFirstCommand.equals(libraryFirstCommandCopy));

        // different types -> returns false
        assertFalse(libraryFirstCommand.equals(1));

        // null -> returns false
        assertFalse(libraryFirstCommand.equals(null));

        // different book -> returns false
        assertFalse(libraryFirstCommand.equals(librarySecondCommand));
    }

    /**
     * Executes a {@code LibraryCommand} with the given {@code index}, and checks that
     * {@code network.searchLibraryForBook(book)} is being called with the correct book.
     */
    private void assertExecutionSuccess(Index index, Book expectedBook, Model expectedModel) {
        LibraryCommand libraryCommand = new LibraryCommand(index);

        NetworkManager networkManagerMock = mock(NetworkManager.class);
        when(networkManagerMock.searchLibraryForBook(expectedBook))
                .thenReturn(CompletableFuture.completedFuture(expectedBook.toString()));

        libraryCommand.setData(model, networkManagerMock, new CommandHistory(), new UndoStack());

        assertCommandSuccess(libraryCommand, model, LibraryCommand.MESSAGE_SEARCHING, expectedModel);
        verify(networkManagerMock).searchLibraryForBook(expectedBook);
    }

    /**
     * Returns a {@code LibraryCommand} with the parameter {@code index}.
     */
    private LibraryCommand prepareCommand(Index index) {
        LibraryCommand command = new LibraryCommand(index);
        command.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return command;
    }
}
