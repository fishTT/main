package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ClearMainContentRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.BookShelf;
import seedu.address.model.ReadOnlyBookShelf;

/**
 * Clears the book shelf.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Book shelf has been cleared!";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Items from the current list cannot be cleared.";

    public static final String UNDO_SUCCESS = "Successfully undone clearing of book shelf.";

    private ReadOnlyBookShelf previousBookShelf;

    @Override
    protected void saveSnapshot() {
        previousBookShelf = new BookShelf(model.getBookShelf());
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        checkActiveListType();

        model.resetData(new BookShelf());
        EventsCenter.getInstance().post(new ClearMainContentRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Throws a {@link CommandException} if the active list type is not supported by this command.
     */
    private void checkActiveListType() throws CommandException {
        requireNonNull(model);

        if (model.getActiveListType() != ActiveListType.BOOK_SHELF) {
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }
    }

    @Override
    protected String undo() {
        requireAllNonNull(model, previousBookShelf);

        model.resetData(previousBookShelf);
        return UNDO_SUCCESS;
    }
}
