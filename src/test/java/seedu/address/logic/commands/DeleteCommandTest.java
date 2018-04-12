package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showBookAtIndex;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_BOOK;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.network.NetworkManager;
import seedu.address.testutil.TypicalBooks;

/**
 * Contains integration tests (interaction with the Model and UndoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Book bookToDelete = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_BOOK_SUCCESS, bookToDelete);

        ModelManager expectedModel = new ModelManager(model.getBookShelf(), new UserPrefs());
        expectedModel.deleteBook(bookToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidActiveListType_throwsCommandException() throws Exception {
        model.setActiveListType(ActiveListType.SEARCH_RESULTS);
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_WRONG_ACTIVE_LIST);

        model.setActiveListType(ActiveListType.RECENT_BOOKS);
        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_WRONG_ACTIVE_LIST);
    }

    @Test
    public void execute_invalidBook_throwsAssertionError() throws Exception {
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);
        deleteCommand.preprocessUndoableCommand();
        model.deleteBook(TypicalBooks.BABYLON_ASHES);
        thrown.expect(AssertionError.class);
        deleteCommand.executeUndoableCommand();
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getDisplayBookList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showBookAtIndex(model, INDEX_FIRST_BOOK);

        Book bookToDelete = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_BOOK_SUCCESS, bookToDelete);

        Model expectedModel = new ModelManager(model.getBookShelf(), new UserPrefs());
        expectedModel.deleteBook(bookToDelete);
        showNoBook(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showBookAtIndex(model, INDEX_FIRST_BOOK);

        Index outOfBoundIndex = INDEX_SECOND_BOOK;
        // ensures that outOfBoundIndex is still in bounds of book shelf list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getBookShelf().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndo_validIndexUnfilteredList_success() throws Exception {
        UndoStack undoStack = new UndoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoStack);
        Book bookToDelete = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);
        Model expectedModel = new ModelManager(model.getBookShelf(), new UserPrefs());

        // delete -> first book deleted
        deleteCommand.execute();
        undoStack.push(deleteCommand);

        // undo -> reverts bookshelf back to previous state and filtered book list to show all books
        assertCommandSuccess(undoCommand, model,
                String.format(DeleteCommand.UNDO_SUCCESS, bookToDelete), expectedModel);
    }

    @Test
    public void executeUndo_invalidIndexUnfilteredList_failure() {
        UndoStack undoStack = new UndoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getDisplayBookList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoStack
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_BOOK_DISPLAYED_INDEX);

        // no commands in undoStack -> undoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Book} from a filtered book.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted book in the
     * unfiltered list is different from the index at the filtered list.
     */
    @Test
    public void executeUndo_validIndexFilteredList_samePersonDeleted() throws Exception {
        UndoStack undoStack = new UndoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoStack);
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_BOOK);
        Model expectedModel = new ModelManager(model.getBookShelf(), new UserPrefs());

        showBookAtIndex(model, INDEX_SECOND_BOOK);
        Book bookToDelete = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        // delete -> deletes second book in unfiltered book list / first book in filtered book list
        deleteCommand.execute();
        undoStack.push(deleteCommand);

        // undo -> reverts book shelf back to previous state and filtered book list to show all books
        assertCommandSuccess(undoCommand, model,
                String.format(DeleteCommand.UNDO_SUCCESS, bookToDelete), expectedModel);

        expectedModel.deleteBook(bookToDelete);
        assertNotEquals(bookToDelete, model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased()));
    }

    @Test
    public void equals() throws Exception {
        DeleteCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_BOOK);
        DeleteCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_BOOK);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_BOOK);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different book -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(index);
        deleteCommand.setData(model, mock(NetworkManager.class), new CommandHistory(), new UndoStack());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no book.
     */
    private void showNoBook(Model model) {
        model.updateBookListFilter(p -> false);

        assertTrue(model.getDisplayBookList().isEmpty());
    }
}
