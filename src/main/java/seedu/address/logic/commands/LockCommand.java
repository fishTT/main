package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ActiveListChangedEvent;
import seedu.address.commons.events.ui.ClearMainContentRequestEvent;
import seedu.address.logic.LockManager;
import seedu.address.model.ActiveListType;
import seedu.address.model.Model;

//@@author 592363789
/**
 * Encrypts the book shelf.
 */
public class LockCommand extends Command {

    public static final String COMMAND_WORD = "lock";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lock the app.\n";

    public static final String MESSAGE_SUCCESS = "Successfully locked the app.";

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display.
     */
    @Override
    public CommandResult execute() {
        model.updateBookListFilter(Model.PREDICATE_HIDE_ALL_BOOKS);
        model.setActiveListType(ActiveListType.BOOK_SHELF);
        EventsCenter.getInstance().post(new ActiveListChangedEvent());
        EventsCenter.getInstance().post(new ClearMainContentRequestEvent());
        LockManager.getInstance().lock();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
