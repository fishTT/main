package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToBookListIndexRequestEvent;
import seedu.address.commons.util.CommandUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ActiveListType;

/**
 * Selects a book identified using it's last displayed index from the book shelf.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the book identified by the index number in the current book listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_BOOK_SUCCESS = "Selected Book: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        CommandUtil.checkValidIndex(model, targetIndex);
        updateRecentBooksList();

        EventsCenter.getInstance().post(new JumpToBookListIndexRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_BOOK_SUCCESS, targetIndex.getOneBased()));

    }

    private void updateRecentBooksList() {
        if (model.getActiveListType() != ActiveListType.RECENT_BOOKS) {
            model.addRecentBook(model.getActiveList().get(targetIndex.getZeroBased()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
