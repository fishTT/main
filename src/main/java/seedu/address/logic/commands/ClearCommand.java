package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ClearMainContentRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;
import seedu.address.model.BookShelf;

/**
 * Clears the book shelf.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Book shelf has been cleared!";
    public static final String MESSAGE_WRONG_ACTIVE_LIST = "Items from the current list cannot be cleared.";


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
        if (model.getActiveListType() != ActiveListType.BOOK_SHELF) {
            throw new CommandException(MESSAGE_WRONG_ACTIVE_LIST);
        }
    }
}
