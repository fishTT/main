package seedu.address.logic.commands;

import static org.mockito.Mockito.mock;
import static seedu.address.logic.UndoStackUtil.prepareStack;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.deleteFirstBook;
import static seedu.address.testutil.TypicalBooks.getTypicalBookShelf;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BOOK;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.book.Book;
import seedu.address.network.NetworkManager;

public class UndoCommandTest {
    private static final NetworkManager MOCK_NETWORK_MANAGER = mock(NetworkManager.class);
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final UndoStack EMPTY_STACK = new UndoStack();

    private final Model model = new ModelManager(getTypicalBookShelf(), new UserPrefs());
    private final DeleteCommand deleteCommandOne = new DeleteCommand(INDEX_FIRST_BOOK);
    private final DeleteCommand deleteCommandTwo = new DeleteCommand(INDEX_FIRST_BOOK);

    @Before
    public void setUp() {
        deleteCommandOne.setData(model, MOCK_NETWORK_MANAGER, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
        deleteCommandTwo.setData(model, MOCK_NETWORK_MANAGER, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
    }

    @Test
    public void execute() throws Exception {
        UndoStack undoStack = prepareStack(
                Arrays.asList(deleteCommandOne, deleteCommandTwo));
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, MOCK_NETWORK_MANAGER, EMPTY_COMMAND_HISTORY, undoStack);
        Book deleteCommandOneBook = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        deleteCommandOne.execute();
        Book deleteCommandTwoBook = model.getDisplayBookList().get(INDEX_FIRST_BOOK.getZeroBased());
        deleteCommandTwo.execute();

        // multiple commands in undoStack
        Model expectedModel = new ModelManager(getTypicalBookShelf(), new UserPrefs());
        deleteFirstBook(expectedModel);
        assertCommandSuccess(undoCommand, model,
                String.format(DeleteCommand.UNDO_SUCCESS, deleteCommandTwoBook), expectedModel);

        // single command in undoStack
        expectedModel = new ModelManager(getTypicalBookShelf(), new UserPrefs());
        assertCommandSuccess(undoCommand, model,
                String.format(DeleteCommand.UNDO_SUCCESS, deleteCommandOneBook), expectedModel);

        // no command in undoStack
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }
}
